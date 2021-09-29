package com.ing.interview.presentation;

import com.ing.interview.model.CarApplicationRequest;
import com.ing.interview.service.CarApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CarApplicationControllerTest {
    @Mock
    private CarApplicationService carApplicationService;
    @InjectMocks
    private CarApplicationController underTest;

    @DisplayName("GIVEN a CarApplicationRequest " +
            "WHEN creating a CarApplication " +
            "THEN call service ")
    @Test
    void createCarApplicationTest(){
        //GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
            .age(10)
            .color("green")
            .model("fiat")
            .build();
        //WHEN
        underTest.createCarApplication(carApplicationRequest);
        //THEN
        verify(carApplicationService, times(1)).createCarApplication(carApplicationRequest);
    }


    @DisplayName("GIVEN an ID " +
            "WHEN getting a CarApplication " +
            "THEN call service ")
    @Test
    void getCarApplicationTest(){
        //GIVEN
        Long id = 10L;
        //WHEN
        underTest.getCarApplication(id);
        //THEN
        verify(carApplicationService, times(1)).getCarApplication(id);
    }


    @DisplayName("GIVEN a colo " +
            "WHEN getting a CarApplication by color " +
            "THEN call service ")
    @Test
    void getCarApplicationByColorTest(){
        //GIVEN
        String color = "blue";
        //WHEN
        underTest.getCarApplicationByColor(color);
        //THEN
        verify(carApplicationService, times(1)).getCarApplicationByColor(color);
    }

}