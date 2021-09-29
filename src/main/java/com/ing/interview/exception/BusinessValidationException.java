package com.ing.interview.exception;

public class BusinessValidationException extends BaseException {
    public BusinessValidationException(String errorKey, String errorMessage) {
        super(errorKey, errorMessage);
    }
}
