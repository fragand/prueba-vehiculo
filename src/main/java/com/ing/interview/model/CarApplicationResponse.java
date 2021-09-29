package com.ing.interview.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ing.interview.infrastructure.dto.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarApplicationResponse {
    private Long id;

    private String color;

    private String model;

    private int age;

    private LocalDate orderDate;

    private OrderStatus orderStatus;

}
