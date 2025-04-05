package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    List<Airplane> findByAirplaneModelContainingIgnoreCase(String keyword);
}
