package com.ing.interview.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ing.interview.exception.BadRequestErrorException;
import com.ing.interview.exception.BusinessValidationException;
import com.ing.interview.integration.util.EndpointUtil;
import com.ing.interview.model.CarApplicationRequest;
import com.ing.interview.model.CarApplicationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarControllerIntegrationTest {
    private static CarApplicationResponse createdCarApplication;

    @Autowired
    private EndpointUtil endpointUtil;

    @DisplayName("GIVEN CarApplicationRequest with no color " +
            "WHEN creating " +
            "THEN return application with default color.")
    @Test
    @Order(1)
    void createCarApplicationWithDefaultColorTest() throws Exception {
        // GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
                .age(21)
                .model("FIAT")
                .build();

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.postCall("/car-applications", carApplicationRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        CarApplicationResponse responseObject = endpointUtil.getResponseObject(mvcResult, CarApplicationResponse.class);
        assertNotNull(responseObject);
        assertNotNull(responseObject.getId());
        createdCarApplication = responseObject;
        assertNotNull(responseObject.getOrderDate());
        assertNotNull(responseObject.getColor());

        assertEquals(carApplicationRequest.getAge(), responseObject.getAge());
        assertEquals(carApplicationRequest.getModel(), responseObject.getModel());
    }

    @DisplayName("GIVEN an existing id " +
            "WHEN getting car application " +
            "THEN return application.")
    @Test
    @Order(2)
    void getCarApplicationByIdTest() throws Exception {
        //GIVEN
        Long id = createdCarApplication.getId();

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.getCall("/car-applications/" + id)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        CarApplicationResponse responseObject = endpointUtil.getResponseObject(mvcResult, CarApplicationResponse.class);
        assertNotNull(responseObject);
        assertNotNull(responseObject.getOrderStatus());
        assertEquals(createdCarApplication.getId(), responseObject.getId());
        assertEquals(createdCarApplication.getAge(), responseObject.getAge());
        assertEquals(createdCarApplication.getColor(), responseObject.getColor());
        assertEquals(createdCarApplication.getModel(), responseObject.getModel());
        assertEquals(createdCarApplication.getOrderDate(), responseObject.getOrderDate());
    }

    @DisplayName("GIVEN CarApplicationRequest  " +
            "WHEN creating " +
            "THEN return application .")
    @Test
    @Order(2)
    void createCarApplicationTest() throws Exception {
        // GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
                .age(21)
                .color("YelloW")
                .model("FIAT")
                .build();

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.postCall("/car-applications", carApplicationRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        CarApplicationResponse responseObject = endpointUtil.getResponseObject(mvcResult, CarApplicationResponse.class);
        assertNotNull(responseObject);
        assertNotNull(responseObject.getId());
        assertNotNull(responseObject.getOrderDate());
        assertNotNull(responseObject.getColor());

        assertEquals(carApplicationRequest.getAge(), responseObject.getAge());
        assertEquals(carApplicationRequest.getModel(), responseObject.getModel());
    }


    @DisplayName("GIVEN a color " +
            "WHEN getting car applications " +
            "THEN return the corresponding applications.")
    @Test
    @Order(3)
    void getCarApplicationsByColorTest() throws Exception {
        //GIVEN
        String color = "YeLloW";

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.getCall("/car-applications/colors/" + color)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        List<CarApplicationResponse> responseObject = endpointUtil.getResponseObject(mvcResult, new TypeReference<List<CarApplicationResponse>>(){});
        assertNotNull(responseObject);
        assertEquals(2 , responseObject.size());

        assertEquals(createdCarApplication.getId(), responseObject.get(0).getId());
        assertEquals(createdCarApplication.getAge(), responseObject.get(0).getAge());
        assertEquals(createdCarApplication.getColor(), responseObject.get(0).getColor());
        assertEquals(createdCarApplication.getModel(), responseObject.get(0).getModel());
        assertEquals(createdCarApplication.getOrderDate(), responseObject.get(0).getOrderDate());

        assertEquals(2, responseObject.get(1).getId());
        assertEquals(createdCarApplication.getAge(), responseObject.get(1).getAge());
        assertEquals(createdCarApplication.getColor(), responseObject.get(1).getColor());
        assertEquals(createdCarApplication.getModel(), responseObject.get(1).getModel());
        assertEquals(createdCarApplication.getOrderDate(), responseObject.get(1).getOrderDate());


    }


    @DisplayName("GIVEN CarApplicationRequest with a color " +
            "WHEN creating but is not available" +
            "THEN throw UNPROCESSABLE_ENTITY.")
    @Test
    void createCarApplicationNotAvailableTest() throws Exception {
        // GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
                .age(21)
                .color("GREEN")
                .model("FIAT")
                .build();

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.postCall("/car-applications", carApplicationRequest)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        BusinessValidationException responseObject = endpointUtil.getResponseObject(mvcResult, BusinessValidationException.class);
        assertNotNull(responseObject);
        assertEquals("car.notAvailable", responseObject.getError().getErrorKey());
        assertEquals("The color and model has not availability.", responseObject.getError().getErrorMessage());
    }

    @DisplayName("GIVEN CarApplicationRequest  " +
            "WHEN creating but is not eligible for insurance" +
            "THEN throw BAD_REQUEST.")
    @Test
    void createCarApplicationNoInsuranceTest() throws Exception {
        // GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
                .age(10)
                .model("FIAT")
                .build();

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.postCall("/car-applications", carApplicationRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        BadRequestErrorException responseObject = endpointUtil.getResponseObject(mvcResult, BadRequestErrorException.class);
        assertNotNull(responseObject);
        assertEquals("car.notEligible.insurance", responseObject.getError().getErrorKey());
        assertEquals("The age and model of the car does not apply for insurance.", responseObject.getError().getErrorMessage());
    }

    @DisplayName("GIVEN CarApplicationRequest with no color " +
            "WHEN creating but there is no color for the model" +
            "THEN throw UNPROCESSABLE_ENTITY.")
    @Test
    void createCarApplicationColorNotAvailableTest() throws Exception {
        // GIVEN
        CarApplicationRequest carApplicationRequest = CarApplicationRequest.builder()
                .age(21)
                .model("FIATS")
                .build();

        //WHEN/THEN
        MvcResult mvcResult = endpointUtil.postCall("/car-applications", carApplicationRequest)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //THEN
        BusinessValidationException responseObject = endpointUtil.getResponseObject(mvcResult, BusinessValidationException.class);
        assertNotNull(responseObject);
        assertEquals("car.notColor", responseObject.getError().getErrorKey());
        assertEquals("No color was selected and no default color for the car.", responseObject.getError().getErrorMessage());
    }


}