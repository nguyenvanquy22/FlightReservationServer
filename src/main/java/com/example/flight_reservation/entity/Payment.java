package com.example.flight_reservation.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.flight_reservation.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "booking_id", nullable = false)
  private Booking booking;

  @Column(name = "amount", precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @Column(name = "payment_method", length = 50)
  private String paymentMethod;

  @Column(name = "transaction_id", length = 50)
  private String transactionId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private PaymentStatus status = PaymentStatus.PENDING;
}
