package com.example.flight_reservation.service.Ticket;

import com.example.flight_reservation.dto.request.TicketRequest;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.entity.Ticket;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.TicketMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.repository.TicketRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    // Create Ticket
    public TicketResponse createTicket(TicketRequest request) {
        Ticket ticket = ticketMapper.toEntity(request);
        ticket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    // Update Ticket
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        // Cập nhật các trường theo request
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setPrice(request.getPrice());
        // Nếu TicketRequest có thêm thông tin về các quan hệ (seatclassAirplaneFlight, passenger, booking) thì cập nhật theo nghiệp vụ
        ticket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    // Delete Ticket
    public ApiResponse<Void> deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
        return new ApiResponse<>(true, "Ticket deleted successfully", null);
    }

    // Get Ticket by ID
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return ticketMapper.toResponse(ticket);
    }

    // Get All Tickets
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toResponse)
                .collect(Collectors.toList());
    }
}
