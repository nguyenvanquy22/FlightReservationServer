package com.example.flight_reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "seatclass_airplane")
@Data
public class SeatClassAirplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatclass_airplane_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seatclass_id", nullable = false)
    private SeatClass seatClass;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private Airplane airplane;

    @Column(name = "row_count", nullable = false)
    private Integer rowCount;

    @Column(name = "column_count", nullable = false)
    private Integer columnCount;
}

