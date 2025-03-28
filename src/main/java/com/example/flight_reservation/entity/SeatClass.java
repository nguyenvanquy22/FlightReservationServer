package com.example.flight_reservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seatclasses")
@Data
public class SeatClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seatclass_id")
    private Long id;

    @Column(name = "seatclass_name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;
}


