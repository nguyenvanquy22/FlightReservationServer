package com.example.flight_reservation.service.SeatClass;

import com.example.flight_reservation.dto.request.SeatClassRequest;
import com.example.flight_reservation.dto.response.SeatClassResponse;
import com.example.flight_reservation.entity.SeatClass;
import com.example.flight_reservation.exception.ResourceNotFoundException;
import com.example.flight_reservation.mapper.SeatClassMapper;
import com.example.flight_reservation.repository.SeatClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatClassService {

    @Autowired
    private SeatClassRepository seatClassRepository;

    @Autowired
    private SeatClassMapper seatClassMapper;

    public SeatClassResponse createSeatClass(SeatClassRequest request) {
        SeatClass seatClass = seatClassMapper.toEntity(request);
        SeatClass saved = seatClassRepository.save(seatClass);
        return seatClassMapper.toResponse(saved);
    }

    public SeatClassResponse updateSeatClass(Long id, SeatClassRequest request) {
        SeatClass seatClass = seatClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SeatClass not found with id " + id));
        seatClass.setName(request.getName());
        seatClass.setDescription(request.getDescription());
        seatClass.setDisplayOrder(request.getDisplayOrder());
        SeatClass updated = seatClassRepository.save(seatClass);
        return seatClassMapper.toResponse(updated);
    }

    public void deleteSeatClass(Long id) {
        if (!seatClassRepository.existsById(id)) {
            throw new ResourceNotFoundException("SeatClass not found with id " + id);
        }
        seatClassRepository.deleteById(id);
    }

    public List<SeatClassResponse> getAllSeatClasses() {
        List<SeatClass> seatClasses = seatClassRepository.findAll(Sort.by("displayOrder").ascending());
        return seatClasses.stream()
                .map(sc -> seatClassMapper.toResponse(sc))
                .collect(Collectors.toList());
    }

    public SeatClassResponse getSeatClassById(Long id) {
        SeatClass seatClass = seatClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SeatClass not found with id " + id));
        return seatClassMapper.toResponse(seatClass);
    }
}

