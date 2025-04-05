package com.example.flight_reservation.service.Airplane;

import com.example.flight_reservation.dto.request.AirplaneRequest;
import com.example.flight_reservation.dto.response.AirplaneResponse;
import com.example.flight_reservation.entity.Airplane;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.AirplaneMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.repository.AirplaneRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private AirplaneMapper airplaneMapper;

    // Create Airplane
    public AirplaneResponse createAirplane(AirplaneRequest request) {
        Airplane airplane = airplaneMapper.toEntity(request);
        airplane = airplaneRepository.save(airplane);
        return airplaneMapper.toResponse(airplane);
    }

    // Update Airplane
    public AirplaneResponse updateAirplane(Long id, AirplaneRequest request) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found with id: " + id));
        // Cập nhật các trường theo request
        airplane.setModel(request.getModel());
        airplane.setRegistrationCode(request.getRegistrationCode());
        airplane.setCapacity(request.getCapacity());
        // Giả sử AirplaneRequest có trường status (kiểu enum) để cập nhật trạng thái
        airplane.setStatus(request.getStatus());
        // Nếu cần cập nhật airline, bạn có thể xử lý thêm tại đây
        airplane = airplaneRepository.save(airplane);
        return airplaneMapper.toResponse(airplane);
    }

    // Delete Airplane
    public ApiResponse<Void> deleteAirplane(Long id) {
        if (!airplaneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airplane not found with id: " + id);
        }
        airplaneRepository.deleteById(id);
        return new ApiResponse<>(true, "Airplane deleted successfully", null);
    }

    // Get Airplane by ID
    public AirplaneResponse getAirplaneById(Long id) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found with id: " + id));
        return airplaneMapper.toResponse(airplane);
    }

    // Get All Airplanes
    public List<AirplaneResponse> getAllAirplanes() {
        return airplaneRepository.findAll()
                .stream()
                .map(airplaneMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Search Airplanes (tìm kiếm theo airplaneModel)
    public List<AirplaneResponse> searchAirplanes(String keyword) {
        return airplaneRepository.findByAirplaneModelContainingIgnoreCase(keyword)
                .stream()
                .map(airplaneMapper::toResponse)
                .collect(Collectors.toList());
    }
}
