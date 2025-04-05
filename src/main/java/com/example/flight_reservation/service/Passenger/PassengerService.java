package com.example.flight_reservation.service.Passenger;

import com.example.flight_reservation.dto.request.PassengerRequest;
import com.example.flight_reservation.dto.response.PassengerResponse;
import com.example.flight_reservation.entity.Passenger;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.PassengerMapper;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.repository.PassengerRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    // Tạo mới Passenger
    public PassengerResponse createPassenger(PassengerRequest request) {
        Passenger passenger = passengerMapper.toEntity(request);
        passenger = passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    // Cập nhật Passenger
    public PassengerResponse updatePassenger(Long id, PassengerRequest request) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setDateOfBirth(request.getDateOfBirth());
        passenger.setPhoneNumber(request.getPhoneNumber());
        passenger.setPassportNumber(request.getPassportNumber());
        passenger = passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    // Xóa Passenger
    public ApiResponse<Void> deletePassenger(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Passenger not found with id: " + id);
        }
        passengerRepository.deleteById(id);
        return new ApiResponse<>(true, "Passenger deleted successfully", null);
    }

    // Lấy thông tin Passenger theo ID
    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
        return passengerMapper.toResponse(passenger);
    }

    // Lấy danh sách tất cả Passenger
    public List<PassengerResponse> getAllPassengers() {
        return passengerRepository.findAll()
                .stream()
                .map(passengerMapper::toResponse)
                .collect(Collectors.toList());
    }
}
