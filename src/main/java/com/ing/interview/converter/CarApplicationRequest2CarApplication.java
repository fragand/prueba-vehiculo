package com.ing.interview.converter;

import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CarApplicationRequest2CarApplication implements Converter<CarApplicationRequest, CarApplication> {
    @Override
    public CarApplication convert(CarApplicationRequest input) {
        return CarApplication.builder()
                .color(input.getColor())
                .model(input.getModel())
                .age(input.getAge())
                .orderDate(LocalDate.now())
                .build();
    }
}
