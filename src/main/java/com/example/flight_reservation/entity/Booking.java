package com.example.flight_reservation.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.flight_reservation.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "booking_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "total_price", precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @Column(name = "booking_date")
  private LocalDateTime bookingDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private BookingStatus status = BookingStatus.PENDING;
}
