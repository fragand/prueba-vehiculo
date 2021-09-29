package com.ing.interview.exception;

public class BadRequestErrorException extends BaseException {
    public BadRequestErrorException(String errorKey, String errorMessage) {
        super(errorKey, errorMessage);
    }
}
