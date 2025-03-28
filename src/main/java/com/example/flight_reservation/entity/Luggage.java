package com.example.flight_reservation.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "luggages")
@Data
public class Luggage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "luggage_id")
  private Long id;

  @Column(name = "luggage_type", length = 50)
  private String luggageType;

  @Column(name = "weight_limit")
  private Integer weightLimit;

  @Column(name = "price", precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "description")
  private String description;
}
