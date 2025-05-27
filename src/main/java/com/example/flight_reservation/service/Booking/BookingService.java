package com.example.flight_reservation.service.Booking;

import com.example.flight_reservation.dto.request.BookingRequest;
import com.example.flight_reservation.dto.request.FlightBookingRequest;
import com.example.flight_reservation.dto.response.BookingResponse;
import com.example.flight_reservation.dto.response.PassengerResponse;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.entity.*;
import com.example.flight_reservation.entity.enums.PaymentStatus;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.BookingMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.PassengerMapper;
import com.example.flight_reservation.mapper.PaymentMapper;
import com.example.flight_reservation.mapper.TicketMapper;
import com.example.flight_reservation.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private SeatClassAirplaneFlightRepository scafRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    // Create Booking
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Tạo mới Booking với ngày giờ hiện tại và trạng thái PENDING
        Booking booking = new Booking();
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        booking.setUser(user);
        booking.setTotalPrice(request.getTotalPrice());
        booking.setBookingDate(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // 2. Tạo và lưu danh sách Passenger
        List<Passenger> passengers = request.getPassengersRequest().stream()
                .map(pr -> {
                    Passenger entity = passengerMapper.toEntity(pr);
                    return passengerRepository.save(entity);
                })
                .toList();

        // 3. Tạo và lưu Ticket cho mỗi Passenger & mỗi flightBookingRequests
        List<Ticket> tickets = new ArrayList<>();
        for (Passenger passenger : passengers) {
            for (FlightBookingRequest fb : request.getFlightBookingRequests()) {
                SeatClassAirplaneFlight scaf = scafRepository.findById(fb.getSeatOptionId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "SeatClassAirplaneFlight not found: " + fb.getSeatOptionId()));
//                Flight flight = flightRepository.findById(fb.getFlightId())
//                        .orElseThrow(() -> new ResourceNotFoundException(
//                                "Flight not found: " + fb.getFlightId()));
                Ticket ticket = new Ticket();
                ticket.setBooking(booking);
                ticket.setPassenger(passenger);
                ticket.setSeatClassAirplaneFlight(scaf);
//                ticket.setFlight(flight);
                // Gán giá từ entity SCAF
                ticket.setPrice(scaf.getSeatPrice());
                // Nếu cần sinh số ghế, có thể đặt null hoặc generate
                ticket.setSeatNumber(null);
                tickets.add(ticketRepository.save(ticket));
            }
        }

        // 4. Tạo Payment record mặc định PENDING
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(request.getTotalPrice());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod("VNPAY");             // hoặc null chờ callback
        payment.setTransactionId(null);
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentRepository.save(payment);

        // 5. Build response
        BookingResponse response = bookingMapper.toResponse(booking);
        response.setTotalPrice(booking.getTotalPrice());
        response.setTickets(
                tickets.stream()
                        .map(ticketMapper::toResponse)
                        .collect(Collectors.toList())
        );
        // Bao gồm luôn thông tin payment
        response.setPayment(paymentMapper.toResponse(payment));

        return response;
    }

    // Update Booking
    @Transactional
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        // Chỉ cho phép update status hoặc totalPrice
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
        booking.setTotalPrice(request.getTotalPrice());
        // Có thể cho update status nếu cần:
        booking.setStatus(request.getStatus());
        booking = bookingRepository.save(booking);

        // Build lại DTO
        BookingResponse resp = bookingMapper.toResponse(booking);
        resp.setTotalPrice(booking.getTotalPrice());
        // map nested tickets
        List<TicketResponse> tickets = ticketRepository.findByBookingId(id).stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
        resp.setTickets(tickets);
        // map payment
        paymentRepository.findByBookingId(id)
                .ifPresent(p -> resp.setPayment(paymentMapper.toResponse(p)));

        return resp;
    }

    @Transactional
    public ApiResponse<Void> deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found: " + id);
        }
        // 1. xóa tickets
        ticketRepository.deleteByBookingId(id);
        // 2. xóa payment
        paymentRepository.findByBookingId(id)
                .ifPresent(paymentRepository::delete);
        // 3. xóa booking
        bookingRepository.deleteById(id);
        return new ApiResponse<>(true, "Booking and related records deleted", null);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(booking -> {
                    BookingResponse resp = bookingMapper.toResponse(booking);
                    resp.setTotalPrice(booking.getTotalPrice());
                    List<TicketResponse> tickets = ticketRepository.findByBookingId(booking.getId()).stream()
                            .map(ticketMapper::toResponse)
                            .collect(Collectors.toList());
                    resp.setTickets(tickets);
                    paymentRepository.findByBookingId(booking.getId())
                            .ifPresent(p -> resp.setPayment(paymentMapper.toResponse(p)));
                    return resp;
                })
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));

        BookingResponse resp = bookingMapper.toResponse(booking);
        resp.setTotalPrice(booking.getTotalPrice());
        List<TicketResponse> tickets = ticketRepository.findByBookingId(id).stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
        resp.setTickets(tickets);

        paymentRepository.findByBookingId(id)
                .ifPresent(p -> resp.setPayment(paymentMapper.toResponse(p)));

        return resp;
    }

    public List<BookingResponse> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream().map(booking -> {
            BookingResponse resp = bookingMapper.toResponse(booking);
            resp.setTotalPrice(booking.getTotalPrice());
            resp.setTickets(
                    ticketRepository.findByBookingId(booking.getId())
                            .stream().map(ticketMapper::toResponse)
                            .collect(Collectors.toList())
            );
            paymentRepository.findByBookingId(booking.getId())
                    .ifPresent(p -> resp.setPayment(paymentMapper.toResponse(p)));
            return resp;
        }).collect(Collectors.toList());
    }
}
