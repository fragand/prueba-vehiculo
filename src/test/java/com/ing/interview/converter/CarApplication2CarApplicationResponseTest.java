package com.ing.interview.converter;

import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CarApplication2CarApplicationResponseTest {

    public static final int AGE = 10;
    public static final String COLOR = "GREEN";
    public static final String MODEL = "BMW";
    public static final long ID = 10L;
    public static final LocalDate ORDER_DATE = LocalDate.now();

    @InjectMocks
    private CarApplication2CarApplicationResponse underTest;

    @DisplayName("GIVEN a car application " +
            "WHEN converting " +
            "THEN return car application response")
    @Test
    void convertTest() {
        //GIVEN
        CarApplication carApplication = CarApplication.builder()
                .age(AGE)
                .color(COLOR)
                .model(MODEL)
                .id(ID)
                .orderDate(ORDER_DATE)
                .build();

        //WHEN
        CarApplicationResponse carApplicationResponse = this.underTest.convert(carApplication);

        //THEN
        assertEquals(AGE, carApplicationResponse.getAge());
        assertEquals(COLOR, carApplicationResponse.getColor());
        assertEquals(MODEL, carApplicationResponse.getModel());
        assertEquals(ID, carApplicationResponse.getId());
        assertEquals(ORDER_DATE, carApplicationResponse.getOrderDate());
    }
}