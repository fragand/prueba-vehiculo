package com.ing.interview.converter;

import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CarApplication2CarApplicationResponse implements Converter<CarApplication, CarApplicationResponse> {
    @Override
    public CarApplicationResponse convert(CarApplication input) {
        return CarApplicationResponse.builder()
                .id(input.getId())
                .color(input.getColor())
                .model(input.getModel())
                .age(input.getAge())
                .orderDate(input.getOrderDate())
                .build();
    }
}
