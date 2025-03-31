package com.example.flight_reservation.entity;

import com.example.flight_reservation.entity.enums.AirplaneStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "airplanes")
@Data
public class Airplane {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "airplane_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "airline_id", nullable = false)
  private Airline airline;

  @Column(name = "airplane_model", length = 50)
  private String model;

  @Column(name = "registration_code", length = 20)
  private String registrationCode;

  @Column(name = "capacity")
  private Integer capacity;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private AirplaneStatus status = AirplaneStatus.ACTIVE;
}

