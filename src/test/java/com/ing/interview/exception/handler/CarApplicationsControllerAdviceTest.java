package com.ing.interview.exception.handler;

import com.ing.interview.exception.BadRequestErrorException;
import com.ing.interview.exception.BusinessValidationException;
import com.ing.interview.exception.CarErrorMSTO;
import com.ing.interview.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CarApplicationsControllerAdviceTest {

    public static final String KEY = "key";
    public static final String MSG = "msg";

    @InjectMocks
    private CarApplicationsControllerAdvice carApplicationsControllerAdvice;

    @DisplayName("GIVEN a BadRequestErrorException " +
            "WHEN handling exception " +
            "THEN return Response entity with the correct error")
    @Test
    void handleBadRequestErrorExceptionTest() {
        //GIVEN
        BadRequestErrorException exception = new BadRequestErrorException(KEY, MSG);

        //WHEN
        ResponseEntity<CarErrorMSTO> responseEntity = carApplicationsControllerAdvice.handleException(exception);

        //THEN
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(KEY, responseEntity.getBody().getErrorKey());
        assertEquals(MSG, responseEntity.getBody().getErrorMessage());

    }

    @DisplayName("GIVEN a Throwable " +
            "WHEN handling exception " +
            "THEN return Response entity with the correct error")
    @Test
    void handleThrowableTest() {
        //GIVEN
        Throwable exception = new Throwable(MSG);

        //WHEN
        ResponseEntity<CarErrorMSTO> responseEntity = carApplicationsControllerAdvice.handleException(exception);

        //THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("UNKNOWN-ERROR", responseEntity.getBody().getErrorKey());
        assertEquals(MSG, responseEntity.getBody().getErrorMessage());
    }

    @DisplayName("GIVEN a BusinessValidationException " +
            "WHEN handling exception " +
            "THEN return Response entity with the correct error")
    @Test
    void handleBusinessValidationExceptionTest() {
        //GIVEN
        BusinessValidationException exception = new BusinessValidationException(KEY, MSG);

        //WHEN
        ResponseEntity<CarErrorMSTO> responseEntity = carApplicationsControllerAdvice.handleException(exception);

        //THEN
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(KEY, responseEntity.getBody().getErrorKey());
        assertEquals(MSG, responseEntity.getBody().getErrorMessage());
    }

    @DisplayName("GIVEN a NotFoundException " +
            "WHEN handling exception " +
            "THEN return Response entity with the correct error")
    @Test
    void handleNotFoundExceptionTest() {
        //GIVEN
        NotFoundException exception = new NotFoundException(KEY, MSG);

        //WHEN
        ResponseEntity<CarErrorMSTO> responseEntity = carApplicationsControllerAdvice.handleException(exception);

        //THEN
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(KEY, responseEntity.getBody().getErrorKey());
        assertEquals(MSG, responseEntity.getBody().getErrorMessage());
    }
}