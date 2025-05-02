package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Airplane;
import com.example.flight_reservation.entity.SeatClassAirplane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatClassAirplaneRepository extends JpaRepository<SeatClassAirplane, Long> {
    void deleteByAirplane(Airplane airplane);
    void deleteByAirplaneId(Long airplaneId);
    List<SeatClassAirplane> findByAirplaneId(Long airplaneId);
}
