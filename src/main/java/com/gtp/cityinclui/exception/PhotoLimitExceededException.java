package com.gtp.cityinclui.exception;

public class PhotoLimitExceededException extends RuntimeException{
    public PhotoLimitExceededException(String message) {
        super(message);
    }
}
