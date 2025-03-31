package com.example.flight_reservation.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LuggageRequest {
    private String luggageType;
    private Integer weightLimit;
    private BigDecimal price;
    private String description;
}
