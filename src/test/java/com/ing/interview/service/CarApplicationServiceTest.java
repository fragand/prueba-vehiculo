package com.ing.interview.service;

import com.ing.interview.converter.CollectionConverter;
import com.ing.interview.exception.BadRequestErrorException;
import com.ing.interview.exception.BusinessValidationException;
import com.ing.interview.exception.NotFoundException;
import com.ing.interview.infrastructure.CarAvailabilityRestConnector;
import com.ing.interview.infrastructure.ColorPickerRestConnector;
import com.ing.interview.infrastructure.InsuranceRestConnector;
import com.ing.interview.infrastructure.OrderStatusRestConnector;
import com.ing.interview.infrastructure.dto.OrderStatus;
import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationRequest;
import com.ing.interview.model.CarApplicationResponse;
import com.ing.interview.repository.CarApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarApplicationServiceTest {
    public static final int AGE = 10;
    public static final String COLOR = "GREEN";
    public static final String MODEL = "BMW";
    public static final long ID = 10L;
    public static final LocalDate ORDER_DATE = LocalDate.now();
    @Mock
    private CarApplicationRepository carApplicationRepository;
    @Mock
    private CarAvailabilityRestConnector carAvailabilityRestConnector;
    @Mock
    private ColorPickerRestConnector colorPickerRestConnector;
    @Mock
    private InsuranceRestConnector insuranceRestConnector;
    @Mock
    private ConversionService conversionService;
    @Mock
    private CollectionConverter collectionConverter;
    @Mock
    private OrderStatusRestConnector orderStatusRestConnector;

    @InjectMocks
    private CarApplicationService underTest;
    

    @DisplayName("GIVEN a CarApplicationRequest " +
            "WHEN creating a CarApplication " +
            "THEN return CarApplicationResponse ")
    @Test
    void createCarApplicationTest(){

        //GIVEN
        boolean available = true;
        boolean withInsurance = true;
        CarApplicationRequest carApplicationRequest = buildCarApplicationRequest(AGE, COLOR, MODEL);
        CarApplication carApplication = buildCarApplication(AGE, COLOR, MODEL, ID, ORDER_DATE);
        CarApplicationResponse carApplicationResponse = buildCarApplicationResponse(AGE, COLOR, MODEL, ID, ORDER_DATE);

        when(conversionService.convert(carApplicationRequest, CarApplication.class)).thenReturn(carApplication);
        when(carAvailabilityRestConnector.available(carApplication.getColor(), carApplication.getModel())).thenReturn(available);
        when(insuranceRestConnector.isEligible(carApplication.getAge(), carApplication.getModel())).thenReturn(withInsurance);
        when(conversionService.convert(carApplication, CarApplicationResponse.class)).thenReturn(carApplicationResponse);

        //WHEN
        carApplicationResponse = underTest.createCarApplication(carApplicationRequest);

        //THEN
        verify(conversionService, times(1)).convert(carApplicationRequest, CarApplication.class);
        verify(conversionService, times(1)).convert(carApplication, CarApplicationResponse.class);
        verify(carApplicationRepository, times(1)).save(carApplication);
        verify(colorPickerRestConnector, times(0)).pickColor(carApplication.getModel());
        verify(carAvailabilityRestConnector, times(1)).available(carApplication.getColor(), carApplication.getModel());
        verify(insuranceRestConnector, times(1)).isEligible(carApplication.getAge(), carApplication.getModel());
        assertEquals(carApplicationRequest.getAge(), carApplicationResponse.getAge());
        assertEquals(carApplicationRequest.getColor(), carApplicationResponse.getColor());
        assertEquals(carApplicationRequest.getModel(), carApplicationResponse.getModel());
        assertNotNull(carApplicationResponse.getOrderDate());
    }


    @DisplayName("GIVEN a CarApplicationRequest " +
            "WHEN creating a CarApplication with NO color " +
            "THEN return CarApplicationResponse with default color")
    @Test
    void createCarApplicationWithDefaultColorTest(){

        //GIVEN
        String color = null;
        boolean available = true;
        boolean withInsurance = true;
        CarApplicationRequest carApplicationRequest = buildCarApplicationRequest(AGE, color, MODEL);
        CarApplication carApplication = buildCarApplication(AGE, color, MODEL, ID, ORDER_DATE);
        CarApplicationResponse carApplicationResponse = buildCarApplicationResponse(AGE, COLOR, MODEL, ID, ORDER_DATE);

        when(conversionService.convert(carApplicationRequest, CarApplication.class)).thenReturn(carApplication);
        when(carAvailabilityRestConnector.available(COLOR, carApplication.getModel())).thenReturn(available);
        when(insuranceRestConnector.isEligible(carApplication.getAge(), carApplication.getModel())).thenReturn(withInsurance);
        when(colorPickerRestConnector.pickColor(carApplication.getModel())).thenReturn(Optional.of(COLOR));
        when(conversionService.convert(carApplication, CarApplicationResponse.class)).thenReturn(carApplicationResponse);

        //WHEN
        carApplicationResponse = underTest.createCarApplication(carApplicationRequest);

        //THEN
        verify(conversionService, times(1)).convert(carApplicationRequest, CarApplication.class);
        verify(conversionService, times(1)).convert(carApplication, CarApplicationResponse.class);
        verify(carApplicationRepository, times(1)).save(carApplication);
        verify(colorPickerRestConnector, times(1)).pickColor(carApplication.getModel());
        verify(carAvailabilityRestConnector, times(1)).available(carApplication.getColor(), carApplication.getModel());
        verify(insuranceRestConnector, times(1)).isEligible(carApplication.getAge(), carApplication.getModel());
        assertEquals(carApplicationRequest.getAge(), carApplicationResponse.getAge());
        assertEquals(COLOR, carApplicationResponse.getColor());
        assertEquals(carApplicationRequest.getModel(), carApplicationResponse.getModel());
        assertNotNull(carApplicationResponse.getOrderDate());
    }

    @DisplayName("GIVEN a CarApplicationRequest " +
            "WHEN creating a CarApplication with NO color " +
            "AND there not default found " +
            "THEN throw BusinessValidationException")
    @Test
    void createCarApplicationWithNoColorAndNoDefaultColorTest(){

        //GIVEN
        String color = null;
        CarApplicationRequest carApplicationRequest = buildCarApplicationRequest(AGE, color, MODEL);
        CarApplication carApplication = buildCarApplication(AGE, color, MODEL, ID, ORDER_DATE);

        when(conversionService.convert(carApplicationRequest, CarApplication.class)).thenReturn(carApplication);
        when(colorPickerRestConnector.pickColor(carApplication.getModel())).thenReturn(Optional.empty());

        //WHEN/THEN
        BusinessValidationException businessValidationException = assertThrows(BusinessValidationException.class, () -> underTest.createCarApplication(carApplicationRequest));
        assertEquals("car.notColor", businessValidationException.getError().getErrorKey());
        verify(conversionService, times(1)).convert(carApplicationRequest, CarApplication.class);
        verify(conversionService, times(0)).convert(carApplication, CarApplicationResponse.class);
        verify(colorPickerRestConnector, times(1)).pickColor(carApplication.getModel());
        verify(carAvailabilityRestConnector, times(0)).available(carApplication.getColor(), carApplication.getModel());
        verify(insuranceRestConnector, times(0)).isEligible(carApplication.getAge(), carApplication.getModel());
        verify(carApplicationRepository, times(0)).save(carApplication);
    }


    @DisplayName("GIVEN a CarApplicationRequest " +
            "WHEN creating a CarApplication with NO availability " +
            "THEN throw BusinessValidationException")
    @Test
    void createCarApplicationWithNoAvailabilityTest(){

        //GIVEN
        boolean available = false;
        CarApplicationRequest carApplicationRequest = buildCarApplicationRequest(AGE, COLOR, MODEL);
        CarApplication carApplication = buildCarApplication(AGE, COLOR, MODEL, ID, ORDER_DATE);

        when(conversionService.convert(carApplicationRequest, CarApplication.class)).thenReturn(carApplication);
        when(carAvailabilityRestConnector.available(carApplication.getColor(), carApplication.getModel())).thenReturn(available);

        //WHEN/THEN
        BusinessValidationException businessValidationException = assertThrows(BusinessValidationException.class, () -> underTest.createCarApplication(carApplicationRequest));
        assertEquals("car.notAvailable", businessValidationException.getError().getErrorKey());
        verify(conversionService, times(1)).convert(carApplicationRequest, CarApplication.class);
        verify(conversionService, times(0)).convert(carApplication, CarApplicationResponse.class);
        verify(colorPickerRestConnector, times(0)).pickColor(carApplication.getModel());
        verify(carAvailabilityRestConnector, times(1)).available(carApplication.getColor(), carApplication.getModel());
        verify(insuranceRestConnector, times(0)).isEligible(carApplication.getAge(), carApplication.getModel());
        verify(carApplicationRepository, times(0)).save(carApplication);
    }


    @DisplayName("GIVEN a CarApplicationRequest " +
            "WHEN creating a CarApplication with NO Insurance " +
            "THEN throw BusinessValidationException")
    @Test
    void createCarApplicationWithNoAInsuranceTest(){

        //GIVEN
        boolean available = true;
        boolean withInsurance = false;
        CarApplicationRequest carApplicationRequest = buildCarApplicationRequest(AGE, COLOR, MODEL);
        CarApplication carApplication = buildCarApplication(AGE, COLOR, MODEL, ID, ORDER_DATE);

        when(conversionService.convert(carApplicationRequest, CarApplication.class)).thenReturn(carApplication);
        when(carAvailabilityRestConnector.available(carApplication.getColor(), carApplication.getModel())).thenReturn(available);
        when(insuranceRestConnector.isEligible(carApplication.getAge(), carApplication.getModel())).thenReturn(withInsurance);

        //WHEN/THEN
        BadRequestErrorException badRequestErrorException = assertThrows(BadRequestErrorException.class, () -> underTest.createCarApplication(carApplicationRequest));
        assertEquals("car.notEligible.insurance", badRequestErrorException.getError().getErrorKey());
        verify(conversionService, times(1)).convert(carApplicationRequest, CarApplication.class);
        verify(conversionService, times(0)).convert(carApplication, CarApplicationResponse.class);
        verify(colorPickerRestConnector, times(0)).pickColor(carApplication.getModel());
        verify(carAvailabilityRestConnector, times(1)).available(carApplication.getColor(), carApplication.getModel());
        verify(insuranceRestConnector, times(1)).isEligible(carApplication.getAge(), carApplication.getModel());
        verify(carApplicationRepository, times(0)).save(carApplication);
    }


    @DisplayName("GIVEN an id " +
            "WHEN getting a car application " +
            "THEN return CarApplicationResponse")
    @Test
    void getCarApplicationTest(){

        //GIVEN
        Long id = 10L;
        CarApplication carApplication = buildCarApplication(AGE, COLOR, MODEL, id, ORDER_DATE);
        CarApplicationResponse carApplicationResponse = buildCarApplicationResponse(AGE, COLOR, MODEL, id, ORDER_DATE);

        when(carApplicationRepository.findById(id)).thenReturn(Optional.of(carApplication));
        when(conversionService.convert(carApplication, CarApplicationResponse.class)).thenReturn(carApplicationResponse);
        when(orderStatusRestConnector.checkOrderStatus(id)).thenReturn(carApplicationResponse.getOrderStatus());

        //WHEN
        CarApplicationResponse response = underTest.getCarApplication(id);

        //THEN
        verify(carApplicationRepository, times(1)).findById(id);
        verify(conversionService, times(1)).convert(carApplication, CarApplicationResponse.class);
        verify(orderStatusRestConnector, times(1)).checkOrderStatus(id);
        assertEquals(carApplicationResponse, response);
    }


    @DisplayName("GIVEN a non existing id " +
            "WHEN getting a car application " +
            "THEN throw NotFoundException")
    @Test
    void getCarApplicationWithNonExistingIdTest(){

        //GIVEN
        Long id = 10L;
        CarApplication carApplication = buildCarApplication(AGE, COLOR, MODEL, id, ORDER_DATE);
        CarApplicationResponse carApplicationResponse = buildCarApplicationResponse(AGE, COLOR, MODEL, id, ORDER_DATE);

        when(carApplicationRepository.findById(id)).thenReturn(Optional.empty());


        //WHEN
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> underTest.getCarApplication(id));
        assertEquals("carApplication.notFound", notFoundException.getError().getErrorKey());

        //THEN
        verify(carApplicationRepository, times(1)).findById(id);
        verify(conversionService, times(0)).convert(carApplication, CarApplicationResponse.class);
        verify(orderStatusRestConnector, times(0)).checkOrderStatus(id);
    }


    @DisplayName("GIVEN a color " +
            "WHEN getting a car application by color" +
            "THEN return a list of car applications")
    @Test
    void getCarApplicationByColorTest(){

        //GIVEN
        CarApplication carApplication = buildCarApplication(AGE, COLOR, MODEL, ID, ORDER_DATE);
        CarApplicationResponse carApplicationResponse = buildCarApplicationResponse(AGE, COLOR, MODEL, ID, ORDER_DATE);
        List<CarApplication> carApplications = Collections.singletonList(carApplication);
        List<CarApplicationResponse> carApplicationResponses = Collections.singletonList(carApplicationResponse);

        when(carApplicationRepository.findCarApplicationsByColorIgnoreCase(COLOR)).thenReturn(carApplications);
        when(collectionConverter.fromListToList(carApplications,CarApplication.class, CarApplicationResponse.class))
                .thenReturn(carApplicationResponses);
        when(orderStatusRestConnector.checkOrderStatus(ID)).thenReturn(carApplicationResponse.getOrderStatus());


        //WHEN
        List<CarApplicationResponse> carApplicationByColor = underTest.getCarApplicationByColor(COLOR);

        //THEN
        verify(carApplicationRepository, times(1)).findCarApplicationsByColorIgnoreCase(COLOR);
        verify(collectionConverter, times(1)).fromListToList(carApplications, CarApplication.class, CarApplicationResponse.class);
        verify(orderStatusRestConnector, times(1)).checkOrderStatus(ID);
        assertEquals(carApplicationResponses, carApplicationByColor);
    }


    private CarApplicationResponse buildCarApplicationResponse(int age, String color, String model,  Long id, LocalDate orderDate) {
        return CarApplicationResponse.builder()
            .age(age)
            .id(id)
            .color(color)
            .model(model)
            .orderDate(orderDate)
            .orderStatus(OrderStatus.builder()
                .assignedTo("dfragan")
                .lastUpdate(LocalDateTime.now())
                .stage("pending")
                .build())
        .build();

    }

    private CarApplication buildCarApplication(int age, String color, String model, Long id, LocalDate orderDate) {
        return CarApplication.builder()
            .age(age)
            .color(color)
            .model(model)
            .id(id)
            .orderDate(orderDate)
            .build();
    }

    private CarApplicationRequest buildCarApplicationRequest(int age, String color, String model) {
        return CarApplicationRequest.builder()
            .age(age)
            .color(color)
            .model(model)
            .build();
    }

}