package com.example.flight_reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ticket_luggage")
@Data
public class TicketLuggage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_luggage_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "luggage_id", nullable = false)
    private Luggage luggage;
}
