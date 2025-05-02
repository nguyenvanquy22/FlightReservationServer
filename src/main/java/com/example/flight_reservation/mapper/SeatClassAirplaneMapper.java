package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.SeatClassAirplaneRequest;
import com.example.flight_reservation.dto.response.SeatClassAirplaneResponse;
import com.example.flight_reservation.entity.SeatClassAirplane;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatClassAirplaneMapper {
    SeatClassAirplane toEntity(SeatClassAirplaneRequest dto);
    @Mapping(target = "seatClassId", source = "seatClass.id")
    @Mapping(target = "seatClassName", source = "seatClass.name")
    @Mapping(target = "airplaneId", source = "airplane.id")
    SeatClassAirplaneResponse toResponse(SeatClassAirplane entity);
    List<SeatClassAirplaneResponse> toResponses(List<SeatClassAirplane> entities);
}
