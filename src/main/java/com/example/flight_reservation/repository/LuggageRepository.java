package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Luggage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuggageRepository extends JpaRepository<Luggage, Long> {
}
