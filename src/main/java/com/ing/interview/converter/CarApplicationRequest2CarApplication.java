package com.ing.interview.converter;

import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;

@Component
public class CarApplicationRequest2CarApplication implements Converter<CarApplicationRequest, CarApplication> {
    @Override
    public CarApplication convert(CarApplicationRequest input) {
        String color = null;
        if (input.getColor() != null){
            color = input.getColor().toUpperCase(Locale.ROOT);
        }

        return CarApplication.builder()
                .color(color)
                .model(input.getModel().toUpperCase(Locale.ROOT))
                .age(input.getAge())
                .orderDate(LocalDate.now())
                .build();
    }
}
