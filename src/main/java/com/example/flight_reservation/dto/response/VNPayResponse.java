package com.example.flight_reservation.dto.response;

import com.example.flight_reservation.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class VNPayResponse {
  private Long bookingId;
  private String message;
  private BookingStatus status;
  private String paymentUrl;
  private PaymentDetails paymentDetails;

  public VNPayResponse(Long bookingId, String message, BookingStatus status, String paymentUrl) {
    this.bookingId = bookingId;
    this.message = message;
    this.status = status;
    this.paymentUrl = paymentUrl;
  }

  public VNPayResponse(Long bookingId, String message, BookingStatus status) {
    this.bookingId = bookingId;
    this.message = message;
    this.status = status;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PaymentDetails {
    private String transactionId;
    private String paymentMethod;
    private Long amount;
    private LocalDateTime paymentDate;
  }
}