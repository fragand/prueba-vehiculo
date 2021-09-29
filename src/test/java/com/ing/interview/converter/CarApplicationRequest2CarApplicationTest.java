package com.ing.interview.converter;

import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CarApplicationRequest2CarApplicationTest {

    public static final int AGE = 10;
    public static final String COLOR = "GREEN";
    public static final String MODEL = "BMW";

    @InjectMocks
    private CarApplicationRequest2CarApplication underTest;

    @DisplayName("GIVEN a car application request " +
            "WHEN converting " +
            "THEN return car application")
    @Test
    void convertTest() {
        //GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
                .age(AGE)
                .color(COLOR)
                .model(MODEL)
                .build();

        //WHEN
        CarApplication carApplication = this.underTest.convert(carApplicationRequest);

        //THEN
        assertEquals(AGE, carApplication.getAge());
        assertEquals(COLOR, carApplication.getColor());
        assertEquals(MODEL, carApplication.getModel());
        assertNull(carApplication.getId());
        assertNotNull(carApplication.getOrderDate());
    }
}