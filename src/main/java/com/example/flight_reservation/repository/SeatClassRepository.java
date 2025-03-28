package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.SeatClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatClassRepository extends JpaRepository<SeatClass, Long> {
}
