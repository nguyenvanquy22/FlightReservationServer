package com.example.flight_reservation.service.Flight;

import com.example.flight_reservation.dto.request.FlightRequest;
import com.example.flight_reservation.dto.response.FlightResponse;
import com.example.flight_reservation.dto.response.SeatClassAirplaneFlightResponse;
import com.example.flight_reservation.dto.response.TransitResponse;
import com.example.flight_reservation.entity.*;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.FlightMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.SeatClassAirplaneFlightMapper;
import com.example.flight_reservation.mapper.TransitMapper;
import com.example.flight_reservation.repository.*;

import java.util.List;
import java.util.stream.Collectors;

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
    private SeatClassAirplaneRepository seatClassAirplaneRepository;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private TransitMapper transitMapper;

    @Autowired
    private SeatClassAirplaneFlightMapper seatClassAirplaneFlightMapper;

    @Autowired private AirplaneRepository airplaneRepository;
    @Autowired private AirportRepository airportRepository;

    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        // 1. Map cơ bản
        Flight flight = flightMapper.toEntity(request);

        // 2. Gán quan hệ
        flight.setAirplane(
                airplaneRepository.findById(request.getAirplaneId())
                        .orElseThrow(() -> new ResourceNotFoundException("Airplane not found: " + request.getAirplaneId()))
        );
        flight.setOriginAirport(
                airportRepository.findById(request.getOriginAirportId())
                        .orElseThrow(() -> new ResourceNotFoundException("Airport not found: " + request.getOriginAirportId()))
        );
        flight.setDestinationAirport(
                airportRepository.findById(request.getDestinationAirportId())
                        .orElseThrow(() -> new ResourceNotFoundException("Airport not found: " + request.getDestinationAirportId()))
        );

        // 3. Save chính để có ID
        Flight saved = flightRepository.save(flight);

        // 4. Nested: Transits
        List<TransitResponse> transits = request.getTransits().stream()
                .map(trDto -> {
                    // map dto → entity
                    Transit tr = transitMapper.toEntity(trDto);

                    // gán quan hệ bắt buộc
                    tr.setFlight(saved);

                    Airport ap = airportRepository.findById(trDto.getAirportId())
                            .orElseThrow(() -> new ResourceNotFoundException("Airport not found: " + trDto.getAirportId()));
                    tr.setTransitAirport(ap);

                    // save & map response
                    Transit savedTr = transitRepository.save(tr);
                    return transitMapper.toResponse(savedTr);
                })
                .collect(Collectors.toList());

        // 5. Nested: Seat options
        List<SeatClassAirplaneFlightResponse> seatOpts = request.getSeatOptions().stream()
                .map(optDto -> {
                    SeatClassAirplaneFlight scaf = seatClassAirplaneFlightMapper.toEntity(optDto);
                    scaf.setFlight(saved);

                    SeatClassAirplane sca = seatClassAirplaneRepository.findById(optDto.getSeatClassAirplaneId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "SeatClassAirplane not found: " + optDto.getSeatClassAirplaneId()));
                    scaf.setSeatClassAirplane(sca);

                    SeatClassAirplaneFlight savedScaf = seatCAFRepository.save(scaf);
                    return seatClassAirplaneFlightMapper.toResponse(savedScaf);
                })
                .collect(Collectors.toList());

        // 6. Build response
        FlightResponse resp = flightMapper.toResponse(saved);
        resp.setTransits(transits);
        resp.setSeatOptions(seatOpts);
        return resp;
    }

    @Transactional
    public FlightResponse updateFlight(Long id, FlightRequest request) {
        // 1. Lấy cũ
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found: " + id));

        // 2. Cập nhật trường cơ bản
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setStatus(request.getStatus());

        // 3. Cập nhật quan hệ nếu thay đổi
        if (!flight.getAirplane().getId().equals(request.getAirplaneId())) {
            flight.setAirplane(
                    airplaneRepository.findById(request.getAirplaneId())
                            .orElseThrow(() -> new ResourceNotFoundException("Airplane not found: " + request.getAirplaneId()))
            );
        }
        if (!flight.getOriginAirport().getId().equals(request.getOriginAirportId())) {
            flight.setOriginAirport(
                    airportRepository.findById(request.getOriginAirportId())
                            .orElseThrow(() -> new ResourceNotFoundException("Airport not found: " + request.getOriginAirportId()))
            );
        }
        if (!flight.getDestinationAirport().getId().equals(request.getDestinationAirportId())) {
            flight.setDestinationAirport(
                    airportRepository.findById(request.getDestinationAirportId())
                            .orElseThrow(() -> new ResourceNotFoundException("Airport not found: " + request.getDestinationAirportId()))
            );
        }

        // 4. Save chính
        Flight updated = flightRepository.save(flight);

        // 5. Xóa nested cũ
        transitRepository.deleteByFlightId(updated.getId());
        seatCAFRepository.deleteByFlightId(updated.getId());

        // 6. Tạo nested mới giống create
        List<TransitResponse> newTransits = request.getTransits().stream()
                .map(trDto -> {
                    Transit tr = transitMapper.toEntity(trDto);
                    tr.setFlight(updated);

                    Airport ap = airportRepository.findById(trDto.getAirportId())
                            .orElseThrow(() -> new ResourceNotFoundException("Airport not found: " + trDto.getAirportId()));
                    tr.setTransitAirport(ap);

                    return transitMapper.toResponse(transitRepository.save(tr));
                }).collect(Collectors.toList());

        List<SeatClassAirplaneFlightResponse> newSeatOpts = request.getSeatOptions().stream()
                .map(optDto -> {
                    SeatClassAirplaneFlight scaf = seatClassAirplaneFlightMapper.toEntity(optDto);
                    scaf.setFlight(updated);

                    SeatClassAirplane sca = seatClassAirplaneRepository.findById(optDto.getSeatClassAirplaneId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "SeatClassAirplane not found: " + optDto.getSeatClassAirplaneId()));
                    scaf.setSeatClassAirplane(sca);

                    return seatClassAirplaneFlightMapper.toResponse(seatCAFRepository.save(scaf));
                }).collect(Collectors.toList());

        // 7. Build response
        FlightResponse resp = flightMapper.toResponse(updated);
        resp.setTransits(newTransits);
        resp.setSeatOptions(newSeatOpts);
        return resp;
    }

    @Transactional
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
