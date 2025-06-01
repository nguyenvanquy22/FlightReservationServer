package com.example.flight_reservation.service.Payment;

import com.example.flight_reservation.config.VNPayConfig;
import com.example.flight_reservation.dto.request.PaymentRequest;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.dto.response.PaymentResponse;
import com.example.flight_reservation.entity.*;
import com.example.flight_reservation.entity.enums.BookingStatus;
import com.example.flight_reservation.entity.enums.PaymentStatus;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.PaymentMapper;
import com.example.flight_reservation.repository.BookingRepository;
import com.example.flight_reservation.repository.PaymentRepository;
import com.example.flight_reservation.repository.TicketRepository;
import com.example.flight_reservation.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private TicketRepository ticketRepository;

    // Tạo record Payment (chưa qua VnPay)
    public PaymentResponse createPayment(PaymentRequest req) {
        Booking booking = bookingRepository.findById(req.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + req.getBookingId()));

        Payment p = paymentMapper.toEntity(req);
        p.setBooking(booking);
        Payment saved = paymentRepository.save(p);
        return paymentMapper.toResponse(saved);
    }

    public PaymentResponse getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse updatePayment(Long id, PaymentRequest req) {
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));

        // Cập nhật fields từ req
        p.setAmount(req.getAmount());
        p.setPaymentDate(req.getPaymentDate());
        p.setPaymentMethod(req.getPaymentMethod());
        p.setTransactionId(req.getTransactionId());
        p.setStatus(req.getStatus());

        Payment updated = paymentRepository.save(p);
        return paymentMapper.toResponse(updated);
    }

    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found: " + id);
        }
        paymentRepository.deleteById(id);
    }

    // Khởi tạo VnPay payment URL
    public ApiResponse<String> createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        String bookingId = request.getParameter("bookingId");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_TxnRef", bookingId);
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + bookingId);
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return new ApiResponse<>(true, "Success", paymentUrl);
    }

    // Xử lý callback từ VnPay
    @Transactional
    public boolean handleVnPayCallback(HttpServletRequest request) {
        String bookingIdStr = request.getParameter("vnp_TxnRef");
        String respCode = request.getParameter("vnp_ResponseCode");
        Long bookingId = Long.parseLong(bookingIdStr);
        // Tìm payment đã tạo trước (PENDING) theo bookingId
        Payment p = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));

        if ("00".equals(respCode)) {
            p.setStatus(PaymentStatus.COMPLETED);
            // cũng cập nhật booking sang CONFIRMED
            p.getBooking().setStatus(BookingStatus.CONFIRMED);
        } else {
            p.setStatus(PaymentStatus.FAILED);
            p.getBooking().setStatus(BookingStatus.PAYMENT_FAILED);
        }
        p.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(p);

        // 3. Nếu thanh toán thành công, gán số ghế tự động cho các Ticket
        if ("00".equals(respCode)) {
            // Lấy danh sách tickets vừa tạo cho booking này
            List<Ticket> newTickets = ticketRepository.findByBookingId(bookingId);

            for (Ticket ticket : newTickets) {
                SeatClassAirplaneFlight scaf = ticket.getSeatClassAirplaneFlight();
                Long scafId = scaf.getId();
                SeatClassAirplane sca = scaf.getSeatClassAirplane();

                // a. Lấy rowCount, columnCount từ SeatClassAirplaneFlight
                int rowCount    = sca.getRowCount();    // ví dụ 25
                int columnCount = sca.getColumnCount(); // ví dụ 6

                // b. Đếm xem đã có bao nhiêu vé của scaf này đã được gán số ghế (đã seatNumber != null)
                int alreadyAssigned = ticketRepository
                        .countBySeatClassAirplaneFlightIdAndSeatNumberIsNotNull(scafId);

                // c. Xác định thứ tự “từ 0” cho vé hiện tại: alreadyAssigned là số ghế đã cấp,
                //    newIndex = alreadyAssigned là chỉ số vé đầu tiên trong newTickets.
                int newIndex = alreadyAssigned;

                // d. Tính hàng và cột dựa trên newIndex:
                //    - hàng = newIndex / columnCount + 1       (vì hàng đếm từ 1)
                //    - cột  = newIndex % columnCount           (vì cột tính từ 0..columnCount-1)
                int rowNumber = (newIndex / columnCount) + 1;
                int colIndex  = newIndex % columnCount;

                // e. Chuyển colIndex sang chữ (A, B, C, …)
                char colLetter = (char) ('A' + colIndex);

                // f. Tạo seatNumber như "1A", "1B", "2A", ...
                String seatNum = rowNumber + String.valueOf(colLetter);
                ticket.setSeatNumber(seatNum);

                // g. Lưu lại ticket
                ticketRepository.save(ticket);
            }
        }

        return "00".equals(respCode);
    }
}
