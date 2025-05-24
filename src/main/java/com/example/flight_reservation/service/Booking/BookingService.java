package com.example.flight_reservation.service.Booking;

import com.example.flight_reservation.dto.request.BookingRequest;
import com.example.flight_reservation.dto.request.FlightBookingRequest;
import com.example.flight_reservation.dto.response.BookingResponse;
import com.example.flight_reservation.dto.response.PassengerResponse;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.entity.*;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.BookingMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.PassengerMapper;
import com.example.flight_reservation.mapper.TicketMapper;
import com.example.flight_reservation.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private SeatClassAirplaneFlightRepository scafRepository;

    // Create Booking
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Tạo mới Booking với ngày giờ hiện tại và trạng thái PENDING
        Booking booking = new Booking();
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        // 2. Tạo và lưu danh sách Passenger
        List<Passenger> passengers = request.getPassengersRequest().stream()
                .map(pr -> {
                    Passenger entity = passengerMapper.toEntity(pr);
                    return passengerRepository.save(entity);
                })
                .toList();

        // 3. Tạo và lưu Ticket cho mỗi Passenger & mỗi flightBookingRequests
        List<Ticket> tickets = new ArrayList<>();
        for (Passenger passenger : passengers) {
            for (FlightBookingRequest fb : request.getFlightBookingRequests()) {
                SeatClassAirplaneFlight scaf = scafRepository.findById(fb.getSeatOptionId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "SeatClassAirplaneFlight not found: " + fb.getSeatOptionId()));
                Ticket ticket = new Ticket();
                ticket.setBooking(booking);
                ticket.setPassenger(passenger);
                ticket.setSeatClassAirplaneFlight(scaf);
                // Gán giá từ entity SCAF
                ticket.setPrice(scaf.getSeatPrice());
                // Nếu cần sinh số ghế, có thể đặt null hoặc generate
                ticket.setSeatNumber(null);
                tickets.add(ticketRepository.save(ticket));
            }
        }

        // 4. Chuẩn bị response DTO (ví dụ)
        BookingResponse response = bookingMapper.toResponse(booking);
        response.setTickets(
                tickets.stream()
                        .map(ticketMapper::toResponse)
                        .collect(Collectors.toList())
        );

        return response;
    }

    // Update Booking
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        // Cập nhật các trường theo request
        // Nếu BookingRequest có thêm thông tin về user, cập nhật quan hệ theo nghiệp vụ (ví dụ: thông qua user id)
        booking = bookingRepository.save(booking);
        return bookingMapper.toResponse(booking);
    }

    // Delete Booking
    public ApiResponse<Void> deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
        return new ApiResponse<>(true, "Booking deleted successfully", null);
    }

    // Get Booking by ID, kèm tickets
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        BookingResponse resp = bookingMapper.toResponse(booking);

        // load và map tickets
        List<TicketResponse> tickets = ticketRepository.findByBookingId(id).stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
        resp.setTickets(tickets);

        return resp;
    }

    // Get All Bookings, kèm tickets cho mỗi booking
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(booking -> {
            BookingResponse resp = bookingMapper.toResponse(booking);

            List<TicketResponse> tickets = ticketRepository.findByBookingId(booking.getId()).stream()
                    .map(ticketMapper::toResponse)
                    .collect(Collectors.toList());
            resp.setTickets(tickets);

            return resp;
        }).collect(Collectors.toList());
    }

}
