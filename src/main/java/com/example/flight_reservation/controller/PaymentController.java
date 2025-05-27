package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.PaymentRequest;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.dto.response.PaymentResponse;
import com.example.flight_reservation.service.Payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    // CRUD cơ bản
    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest req) {
        PaymentResponse resp = paymentService.createPayment(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponse> update(@PathVariable Long id,
                                                  @RequestBody PaymentRequest req) {
        return ResponseEntity.ok(paymentService.updatePayment(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // Khởi tạo VnPay
    @GetMapping("/vn-pay")
    public ResponseEntity<?> payWithVnPay(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public RedirectView vnPayCallback(HttpServletRequest request) {
        boolean ok = paymentService.handleVnPayCallback(request);
//        if (ok) {
//            return ResponseEntity.ok(new ApiResponse<>(true, "Payment successful", null));
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(false, "Payment failed", null));
//        }

        // Lấy bookingId từ tham số vnp_TxnRef (transaction ref)
        String bookingId = request.getParameter("vnp_TxnRef");

        String targetUrl;
        if (ok) {
            targetUrl = "http://localhost:3000/booking-success?bookingId=" + bookingId;
        } else {
            targetUrl = "http://localhost:3000/booking-fail";
        }

        RedirectView rv = new RedirectView();
        rv.setUrl(targetUrl);
        return rv;
    }
}
