package com.example.flight_reservation.service.Airport;

import com.example.flight_reservation.dto.request.AirportRequest;
import com.example.flight_reservation.dto.response.AirportResponse;
import com.example.flight_reservation.entity.Airport;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.AirportMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.repository.AirportRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private AirportMapper airportMapper;

    // Create Airport
    public AirportResponse createAirport(AirportRequest request) {
        Airport airport = airportMapper.toEntity(request);
        airport = airportRepository.save(airport);
        return airportMapper.toResponse(airport);
    }

    // Update Airport
    public AirportResponse updateAirport(Long id, AirportRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
        // Cập nhật các trường theo request
        airport.setName(request.getName());
        airport.setIataCode(request.getIataCode());
        airport.setIcaoCode(request.getIcaoCode());
        airport.setCity(request.getCity());
        airport.setCountry(request.getCountry());
        airport.setAddress(request.getAddress());
        airport = airportRepository.save(airport);
        return airportMapper.toResponse(airport);
    }

    // Delete Airport
    public ApiResponse<Void> deleteAirport(Long id) {
        if (!airportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airport not found with id: " + id);
        }
        airportRepository.deleteById(id);
        return new ApiResponse<>(true, "Airport deleted successfully", null);
    }

    // Get Airport by ID
    public AirportResponse getAirportById(Long id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
        return airportMapper.toResponse(airport);
    }

    // Get All Airports
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll()
                .stream()
                .map(airportMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Search Airports (ví dụ: tìm kiếm theo tên sân bay hoặc thành phố)
    public List<AirportResponse> searchAirports(String keyword) {
        return airportRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(airportMapper::toResponse)
                .collect(Collectors.toList());
    }
}
