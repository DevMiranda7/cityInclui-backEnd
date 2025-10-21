package com.gtp.cityinclui.exception;

import com.gtp.cityinclui.errorDTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExcepitonHandler {

    @ExceptionHandler(EmailJaExistenteException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> emailJaExistenteHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(CloudStorageException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> cloudStorageHandlerException(RuntimeException ex) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(UsuarioNaoExistenteException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> UsuarioNaoExistenteHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

}
