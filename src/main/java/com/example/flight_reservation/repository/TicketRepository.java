package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByBookingId(Long bookingId);
    void deleteByBookingId(Long bookingId);
    int countBySeatClassAirplaneFlightIdAndSeatNumberIsNotNull(Long id);
    List<Ticket> findBySeatClassAirplaneFlight_Id(Long seatClassAirplaneFlightId);
}
