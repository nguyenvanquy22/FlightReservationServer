package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Transit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransitRepository extends JpaRepository<Transit, Long> {
    void deleteByFlightId(Long flightId);
    List<Transit> findByFlightId(Long flightId);
}
