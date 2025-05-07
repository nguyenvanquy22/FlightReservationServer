package com.example.flight_reservation.service.Booking;

import com.example.flight_reservation.dto.request.BookingRequest;
import com.example.flight_reservation.dto.response.BookingResponse;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.entity.Booking;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.BookingMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.TicketMapper;
import com.example.flight_reservation.repository.BookingRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.flight_reservation.repository.TicketRepository;
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

    // Create Booking
    public BookingResponse createBooking(BookingRequest request) {
        Booking booking = bookingMapper.toEntity(request);
        booking = bookingRepository.save(booking);
        return bookingMapper.toResponse(booking);
    }

    // Update Booking
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        // Cập nhật các trường theo request
        booking.setBookingDate(request.getBookingDate());
        booking.setStatus(request.getStatus());
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
