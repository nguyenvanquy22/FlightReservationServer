package com.example.flight_reservation.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "transits")
@Data
public class Transit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transit_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "flight_id", nullable = false)
  private Flight flight;

  @ManyToOne
  @JoinColumn(name = "transit_airport_id", nullable = false)
  private Airport transitAirport;

  @Column(name = "arrival_time")
  private LocalDateTime arrivalTime;

  @Column(name = "departure_time")
  private LocalDateTime departureTime;

  @Column(name = "transit_order")
  private Integer transitOrder;
}
