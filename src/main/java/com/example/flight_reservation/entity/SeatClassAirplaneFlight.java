package com.example.flight_reservation.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seatclass_airplane_flight")
@Data
public class SeatClassAirplaneFlight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatclass_airplane_flight_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seatclass_airplane_id", nullable = false)
    private SeatClassAirplane seatClassAirplane;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "seat_price", precision = 10, scale = 2)
    private BigDecimal seatPrice;
}

