package com.example.flight_reservation.service.Flight;

import com.example.flight_reservation.dto.request.FlightRequest;
import com.example.flight_reservation.dto.response.FlightResponse;
import com.example.flight_reservation.dto.response.SeatClassAirplaneFlightResponse;
import com.example.flight_reservation.dto.response.TransitResponse;
import com.example.flight_reservation.entity.Flight;
import com.example.flight_reservation.entity.SeatClassAirplaneFlight;
import com.example.flight_reservation.entity.Transit;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.FlightMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.SeatClassAirplaneFlightMapper;
import com.example.flight_reservation.mapper.TransitMapper;
import com.example.flight_reservation.repository.FlightRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.flight_reservation.repository.SeatClassAirplaneFlightRepository;
import com.example.flight_reservation.repository.TransitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TransitRepository transitRepository;

    @Autowired
    private SeatClassAirplaneFlightRepository seatCAFRepository;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private TransitMapper transitMapper;

    @Autowired
    private SeatClassAirplaneFlightMapper seatClassAirplaneFlightMapper;

    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        // 1. Map và lưu Flight chính
        Flight flight = flightMapper.toEntity(request);
        Flight savedFlight = flightRepository.save(flight);

        // 2. Lưu các Transit
        List<TransitResponse> savedTransits = request.getTransits().stream()
                .map(tr -> {
                    Transit entity = transitMapper.toEntity(tr);
                    entity.setFlight(savedFlight);
                    return transitMapper.toResponse(transitRepository.save(entity));
                })
                .collect(Collectors.toList());

        // 3. Lưu các SeatClassAirplaneFlight
        List<SeatClassAirplaneFlightResponse> savedSeatOptions = request.getSeatOptions().stream()
                .map(opt -> {
                    SeatClassAirplaneFlight entity = seatClassAirplaneFlightMapper.toEntity(opt);
                    entity.setFlight(savedFlight);
                    return seatClassAirplaneFlightMapper.toResponse(seatCAFRepository.save(entity));
                })
                .collect(Collectors.toList());

        FlightResponse flightResponse = flightMapper.toResponse(savedFlight);
        // 4. Gắn nested vào entity để mapper trả về
        flightResponse.setTransits(savedTransits);
        flightResponse.setSeatOptions(savedSeatOptions);

        // 5. Map trả về DTO
        return flightResponse;
    }

    @Transactional
    public FlightResponse updateFlight(Long id, FlightRequest request) {
        // 1. Lấy Flight cũ
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        // 2. Cập nhật field cơ bản
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setStatus(request.getStatus());
        // (nếu đổi airplane/origin/destination thì cập nhật thêm)

        Flight updatedFlight = flightRepository.save(flight);

        // 3. Xóa nested cũ
        transitRepository.deleteByFlightId(updatedFlight.getId());
        seatCAFRepository.deleteByFlightId(updatedFlight.getId());

        // 4. Tạo lại nested mới (giống create)
        List<TransitResponse> newTransits = request.getTransits().stream()
                .map(tr -> {
                    Transit entity = transitMapper.toEntity(tr);
                    entity.setFlight(updatedFlight);
                    return transitMapper.toResponse(transitRepository.save(entity));
                })
                .collect(Collectors.toList());

        List<SeatClassAirplaneFlightResponse> newSeatOptions = request.getSeatOptions().stream()
                .map(opt -> {
                    SeatClassAirplaneFlight entity = seatClassAirplaneFlightMapper.toEntity(opt);
                    entity.setFlight(updatedFlight);
                    return seatClassAirplaneFlightMapper.toResponse(seatCAFRepository.save(entity));
                })
                .collect(Collectors.toList());

        FlightResponse flightResponse = flightMapper.toResponse(updatedFlight);
        flightResponse.setTransits(newTransits);
        flightResponse.setSeatOptions(newSeatOptions);

        return flightResponse;
    }

    public ApiResponse<Void> deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flight not found with id: " + id);
        }
        // xóa nested trước (nếu không cascade)
        transitRepository.deleteByFlightId(id);
        seatCAFRepository.deleteByFlightId(id);
        flightRepository.deleteById(id);
        return new ApiResponse<>(true, "Flight deleted successfully", null);
    }

    public FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        // Map flight itself
        FlightResponse response = flightMapper.toResponse(flight);

        // Map nested Transit
        List<TransitResponse> transits = transitRepository.findByFlightId(id).stream()
                .map(transitMapper::toResponse)
                .collect(Collectors.toList());
        response.setTransits(transits);

        // Map nested SeatClassAirplaneFlight
        List<SeatClassAirplaneFlightResponse> seatOptions = seatCAFRepository.findByFlightId(id)
                .stream()
                .map(seatClassAirplaneFlightMapper::toResponse)
                .collect(Collectors.toList());
        response.setSeatOptions(seatOptions);

        return response;
    }

    public List<FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream().map(flight -> {
            FlightResponse resp = flightMapper.toResponse(flight);

            List<TransitResponse> transits = transitRepository.findByFlightId(flight.getId()).stream()
                    .map(transitMapper::toResponse)
                    .collect(Collectors.toList());
            resp.setTransits(transits);

            List<SeatClassAirplaneFlightResponse> seatOptions = seatCAFRepository.findByFlightId(flight.getId()).stream()
                    .map(seatClassAirplaneFlightMapper::toResponse)
                    .collect(Collectors.toList());
            resp.setSeatOptions(seatOptions);

            return resp;
        }).collect(Collectors.toList());
    }

    public List<FlightResponse> searchFlights(String keyword) {
        return flightRepository.findByFlightNumberContainingIgnoreCase(keyword).stream().map(flight -> {
            FlightResponse resp = flightMapper.toResponse(flight);

            List<TransitResponse> transits = transitRepository.findByFlightId(flight.getId()).stream()
                    .map(transitMapper::toResponse)
                    .collect(Collectors.toList());
            resp.setTransits(transits);

            List<SeatClassAirplaneFlightResponse> seatOptions = seatCAFRepository.findByFlightId(flight.getId()).stream()
                    .map(seatClassAirplaneFlightMapper::toResponse)
                    .collect(Collectors.toList());
            resp.setSeatOptions(seatOptions);

            return resp;
        }).collect(Collectors.toList());
    }
}
