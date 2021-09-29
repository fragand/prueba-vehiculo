package com.ing.interview.exception;

public class InternalServerErrorException extends BaseException {
    public InternalServerErrorException(String errorKey, String errorMessage) {
        super(errorKey, errorMessage);
    }
}
