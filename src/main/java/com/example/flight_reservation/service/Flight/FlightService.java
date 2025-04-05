package com.example.flight_reservation.service.Flight;

import com.example.flight_reservation.dto.request.FlightRequest;
import com.example.flight_reservation.dto.response.FlightResponse;
import com.example.flight_reservation.entity.Flight;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.FlightMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.repository.FlightRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightMapper flightMapper;

    // Create Flight
    public FlightResponse createFlight(FlightRequest request) {
        Flight flight = flightMapper.toEntity(request);
        flight = flightRepository.save(flight);
        return flightMapper.toResponse(flight);
    }

    // Update Flight
    public FlightResponse updateFlight(Long id, FlightRequest request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        // Cập nhật các trường theo request (giả định FlightRequest chứa các trường cần thiết)
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setStatus(request.getStatus());
        // Nếu FlightRequest có thông tin về airplane, originAirport, destinationAirport (ví dụ: bằng id)
        // Bạn có thể cập nhật các quan hệ này theo logic nghiệp vụ của dự án.
        flight = flightRepository.save(flight);
        return flightMapper.toResponse(flight);
    }

    // Delete Flight
    public ApiResponse<Void> deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
        return new ApiResponse<>(true, "Flight deleted successfully", null);
    }

    // Get Flight by ID
    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return flightMapper.toResponse(flight);
    }

    // Get All Flights
    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll()
                .stream()
                .map(flightMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Search Flights (ví dụ: tìm kiếm theo flightNumber)
    public List<FlightResponse> searchFlights(String keyword) {
        return flightRepository.findByFlightNumberContainingIgnoreCase(keyword)
                .stream()
                .map(flightMapper::toResponse)
                .collect(Collectors.toList());
    }
}
