package com.example.flight_reservation.dto.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class PassengerRequest {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String phoneNumber;
  private String passportNumber;
}