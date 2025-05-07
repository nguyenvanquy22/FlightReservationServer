package com.example.flight_reservation.service.Airplane;

import com.example.flight_reservation.dto.request.AirplaneRequest;
import com.example.flight_reservation.dto.request.SeatClassAirplaneRequest;
import com.example.flight_reservation.dto.response.AirplaneResponse;
import com.example.flight_reservation.entity.Airline;
import com.example.flight_reservation.entity.Airplane;
import com.example.flight_reservation.entity.SeatClass;
import com.example.flight_reservation.entity.SeatClassAirplane;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.AirplaneMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.SeatClassAirplaneMapper;
import com.example.flight_reservation.repository.AirlineRepository;
import com.example.flight_reservation.repository.AirplaneRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.flight_reservation.repository.SeatClassAirplaneRepository;
import com.example.flight_reservation.repository.SeatClassRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private SeatClassAirplaneRepository seatClassAirplaneRepository;

    @Autowired
    private SeatClassRepository seatClassRepository;

    @Autowired
    private SeatClassAirplaneMapper seatClassAirplaneMapper;
    @Autowired
    private AirplaneMapper airplaneMapper;

    // Create Airplane
    @Transactional
    public AirplaneResponse createAirplane(AirplaneRequest request) {
        // 1. Lưu Airplane
        Airplane airplane = airplaneMapper.toEntity(request);

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Airline not found with id: " + request.getAirlineId()));
        airplane.setAirline(airline);

        Airplane saved = airplaneRepository.save(airplane);

        // 2. Lưu danh sách SeatClassAirplane
        List<SeatClassAirplane> configs = request.getSeatClassConfigs().stream()
                .map(dto -> {
                    SeatClass seatclass = seatClassRepository.findById(dto.getSeatClassId())
                            .orElseThrow(() -> new ResourceNotFoundException("Seat class not found: " + dto.getSeatClassId()));
                    SeatClassAirplane ent = seatClassAirplaneMapper.toEntity(dto);
                    ent.setAirplane(saved);
                    ent.setSeatClass(seatclass);
                    return seatClassAirplaneRepository.save(ent);
                })
                .collect(Collectors.toList());

        // 3. Đính list vào entity để mapper xử lý nested response
        saved.setSeatClassAirplanes(configs);

        return airplaneMapper.toResponse(saved);
    }

    // Update Airplane
    @Transactional
    public AirplaneResponse updateAirplane(Long id, AirplaneRequest request) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found with id: " + id));

        // 1. Cập nhật các trường cơ bản
        airplane.setModel(request.getModel());
        airplane.setRegistrationCode(request.getRegistrationCode());
        airplane.setStatus(request.getStatus());
        if (request.getAirlineId() != null) {
            Airline airline = airlineRepository.findById(request.getAirlineId())
                    .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + request.getAirlineId()));
            airplane.setAirline(airline);
        }

        // 2. Xóa toàn bộ cấu hình cũ bằng cách clear() trên collection
        airplane.getSeatClassAirplanes().clear();

        // 3. Thêm lại các cấu hình mới
        if (request.getSeatClassConfigs() != null) {
            for (SeatClassAirplaneRequest dto : request.getSeatClassConfigs()) {
                SeatClass seatClass = seatClassRepository.findById(dto.getSeatClassId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Seat class not found with id: " + dto.getSeatClassId()));

                SeatClassAirplane ent = seatClassAirplaneMapper.toEntity(dto);
                ent.setAirplane(airplane);
                ent.setSeatClass(seatClass);

                // Thêm trực tiếp vào collection managed
                airplane.getSeatClassAirplanes().add(ent);
            }
        }

        // 4. Save chính đối tượng Airplane; do cascade, các child sẽ được persist và orphan được delete
        Airplane updated = airplaneRepository.save(airplane);

        // 5. Chuyển về DTO
        return airplaneMapper.toResponse(updated);
    }

    // Delete Airplane
    public ApiResponse<Void> deleteAirplane(Long id) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found with id: " + id));

        seatClassAirplaneRepository.deleteByAirplaneId(id); // xóa hạng ghế máy bay
        airplaneRepository.delete(airplane);

        return new ApiResponse<>(true, "Airplane deleted successfully", null);
    }

    // Get Airplane by ID
    public AirplaneResponse getAirplaneById(Long id) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found with id: " + id));

        AirplaneResponse response = airplaneMapper.toResponse(airplane);
        List<SeatClassAirplane> seatClasses = seatClassAirplaneRepository.findByAirplaneId(id);

        if (!seatClasses.isEmpty()) {
            response.setSeatClassAirplanes(seatClassAirplaneMapper.toResponses(seatClasses));
        }
        return response;
    }

    // Get All Airplanes
    public List<AirplaneResponse> getAllAirplanes() {
        return airplaneRepository.findAll().stream().map(airplane -> {
            AirplaneResponse response = airplaneMapper.toResponse(airplane);
            List<SeatClassAirplane> seatClasses = seatClassAirplaneRepository.findByAirplaneId(airplane.getId());

            if (!seatClasses.isEmpty()) {
                response.setSeatClassAirplanes(seatClassAirplaneMapper.toResponses(seatClasses));
            }
            return response;
        }).collect(Collectors.toList());
    }

    // Search Airplanes (tìm kiếm theo airplaneModel)
    public List<AirplaneResponse> searchAirplanes(String keyword) {
        return airplaneRepository.findByModelContainingIgnoreCase(keyword).stream().map(airplane -> {
            AirplaneResponse response = airplaneMapper.toResponse(airplane);
            List<SeatClassAirplane> seatClasses = seatClassAirplaneRepository.findByAirplaneId(airplane.getId());

            if (!seatClasses.isEmpty()) {
                response.setSeatClassAirplanes(seatClassAirplaneMapper.toResponses(seatClasses));
            }
            return response;
        }).collect(Collectors.toList());
    }
}
