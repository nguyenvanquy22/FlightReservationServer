package com.example.flight_reservation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.flight_reservation.entity.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private String transactionId;
    private PaymentStatus status;
}