package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.TicketLuggage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketLuggageRepository extends JpaRepository<TicketLuggage, Long> {
}
