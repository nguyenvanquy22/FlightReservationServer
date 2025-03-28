package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.SeatClassAirplaneFlight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatClassAirplaneFlightRepository extends JpaRepository<SeatClassAirplaneFlight, Long> {
}
