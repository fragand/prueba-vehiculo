package com.ing.interview.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private CarErrorMSTO error;

    public BaseException(final String errorKey, final String errorMessage) {
        super(errorMessage);
        error = CarErrorMSTO.builder()
                .errorKey(errorKey)
                .errorMessage(errorMessage)
                .build();
    }
}