package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.SeatClassAirplane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatClassAirplaneRepository extends JpaRepository<SeatClassAirplane, Long> {
}
