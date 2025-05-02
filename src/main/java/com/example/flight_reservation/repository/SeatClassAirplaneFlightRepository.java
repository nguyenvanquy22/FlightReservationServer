package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.SeatClassAirplaneFlight;
import com.example.flight_reservation.entity.Transit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatClassAirplaneFlightRepository extends JpaRepository<SeatClassAirplaneFlight, Long> {
    void deleteByFlightId(Long flightId);
    List<SeatClassAirplaneFlight> findByFlightId(Long flightId);
}
