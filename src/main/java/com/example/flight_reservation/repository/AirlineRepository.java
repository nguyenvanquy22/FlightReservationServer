package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    List<Airline> findByNameContainingIgnoreCase(String keyword);
}
