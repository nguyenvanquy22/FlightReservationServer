package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findByAirportNameContainingIgnoreCase(String keyword);
}
