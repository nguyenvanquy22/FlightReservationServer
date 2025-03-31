package com.example.flight_reservation.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LuggageResponse {
    private Long id;
    private String luggageType;
    private Integer weightLimit;
    private BigDecimal price;
    private String description;
}