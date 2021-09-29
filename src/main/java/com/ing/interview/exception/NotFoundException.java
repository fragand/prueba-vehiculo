package com.ing.interview.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String errorKey, String errorMessage) {
        super(errorKey, errorMessage);
    }
}
