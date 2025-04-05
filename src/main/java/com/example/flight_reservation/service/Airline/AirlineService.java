package com.example.flight_reservation.service.Airline;

import com.example.flight_reservation.dto.request.AirlineRequest;
import com.example.flight_reservation.dto.response.AirlineResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.entity.Airline;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.AirlineMapper;
import com.example.flight_reservation.repository.AirlineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirlineMapper airlineMapper;

    // Create
    public AirlineResponse createAirline(AirlineRequest request) {
        Airline airline = airlineMapper.toEntity(request);
        airline = airlineRepository.save(airline);
        return airlineMapper.toResponse(airline);
    }

    // Update
    public AirlineResponse updateAirline(Long id, AirlineRequest request) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
        // Cập nhật các trường theo request
        airline.setName(request.getName());
        airline.setIataCode(request.getIataCode());
        airline.setIcaoCode(request.getIcaoCode());
        airline = airlineRepository.save(airline);
        return airlineMapper.toResponse(airline);
    }

    // Delete
    public ApiResponse<Void> deleteAirline(Long id) {
        if (!airlineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airline not found with id: " + id);
        }
        airlineRepository.deleteById(id);
        return new ApiResponse<>(true, "Airline deleted successfully", null);
    }

    // Get by ID
    public AirlineResponse getAirlineById(Long id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
        return airlineMapper.toResponse(airline);
    }

    // Get all
    public List<AirlineResponse> getAllAirlines() {
        return airlineRepository.findAll()
                .stream()
                .map(airlineMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Search (tìm kiếm theo tên, không phân biệt chữ hoa chữ thường)
    public List<AirlineResponse> searchAirlines(String keyword) {
        return airlineRepository.findByAirlineNameContainingIgnoreCase(keyword)
                .stream()
                .map(airlineMapper::toResponse)
                .collect(Collectors.toList());
    }
}