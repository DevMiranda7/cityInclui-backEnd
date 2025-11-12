package com.gtp.cityinclui.exception;

public class AuthenticationRequiredException extends RuntimeException{
    public AuthenticationRequiredException(String message) {
        super(message);
    }
}
