package com.example.flight_reservation.entity;

import com.example.flight_reservation.entity.enums.AirplaneStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private AirplaneStatus status = AirplaneStatus.ACTIVE;

  @OneToMany(mappedBy = "airplane", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SeatClassAirplane> seatClassAirplanes = new ArrayList<>();
}

