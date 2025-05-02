package com.example.flight_reservation.service.Airplane;

import com.example.flight_reservation.dto.request.AirplaneRequest;
import com.example.flight_reservation.dto.response.AirplaneResponse;
import com.example.flight_reservation.entity.Airplane;
import com.example.flight_reservation.entity.SeatClassAirplane;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.AirplaneMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.mapper.SeatClassAirplaneMapper;
import com.example.flight_reservation.repository.AirplaneRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.example.flight_reservation.repository.SeatClassAirplaneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private SeatClassAirplaneRepository seatClassAirplaneRepository;

    @Autowired
    private SeatClassAirplaneMapper seatClassAirplaneMapper;
    @Autowired
    private AirplaneMapper airplaneMapper;

    // Create Airplane
    @Transactional
    public AirplaneResponse createAirplane(AirplaneRequest request) {
        // 1. Lưu Airplane
        Airplane airplane = airplaneMapper.toEntity(request);
        Airplane saved = airplaneRepository.save(airplane);

        // 2. Lưu danh sách SeatClassAirplane
        List<SeatClassAirplane> configs = request.getSeatClassConfigs().stream()
                .map(dto -> {
                    SeatClassAirplane ent = seatClassAirplaneMapper.toEntity(dto);
                    ent.setAirplane(saved);
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

        // Cập nhật các trường cơ bản
        airplane.setModel(request.getModel());
        airplane.setRegistrationCode(request.getRegistrationCode());
        airplane.setCapacity(request.getCapacity());
        airplane.setStatus(request.getStatus());
        Airplane updated = airplaneRepository.save(airplane);

        // Xóa hết cấu hình cũ
        seatClassAirplaneRepository.deleteByAirplane(updated);

        // Thêm lại cấu hình mới
        List<SeatClassAirplane> configs = request.getSeatClassConfigs().stream()
                .map(dto -> {
                    SeatClassAirplane ent = seatClassAirplaneMapper.toEntity(dto);
                    ent.setAirplane(updated);
                    return seatClassAirplaneRepository.save(ent);
                })
                .collect(Collectors.toList());

        updated.setSeatClassAirplanes(configs);
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
