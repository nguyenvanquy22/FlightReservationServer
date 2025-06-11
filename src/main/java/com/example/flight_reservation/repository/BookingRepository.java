package com.example.flight_reservation.repository;

import com.example.flight_reservation.entity.Booking;
import com.example.flight_reservation.entity.User;
import com.example.flight_reservation.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
}
