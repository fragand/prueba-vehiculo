package com.ing.interview.exception.handler;

import com.ing.interview.exception.BadRequestErrorException;
import com.ing.interview.exception.BusinessValidationException;
import com.ing.interview.exception.CarErrorMSTO;
import com.ing.interview.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CarApplicationsControllerAdvice {
    @ExceptionHandler(BadRequestErrorException.class)
    public ResponseEntity<CarErrorMSTO> handleException(BadRequestErrorException ex) {
        return new ResponseEntity<>(ex.getError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CarErrorMSTO> handleException(Throwable ex) {
        return new ResponseEntity<>(
            CarErrorMSTO
                .builder()
                .errorKey("UNKNOWN-ERROR")
                .errorMessage(ex.getMessage())
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<CarErrorMSTO> handleException(BusinessValidationException ex) {
        return new ResponseEntity<>(ex.getError(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CarErrorMSTO> handleException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getError(), HttpStatus.NOT_FOUND);
    }
}
