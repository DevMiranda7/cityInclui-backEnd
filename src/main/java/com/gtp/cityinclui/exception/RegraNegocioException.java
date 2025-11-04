package com.gtp.cityinclui.exception;
import org.springframework.http.HttpStatus;
import  org.springframework.web.bind.annotation.ResponseStatus;
public class RegraNegocioException  extends  RuntimeException {
    public RegraNegocioException(String message){
        super(message);
    }
}
