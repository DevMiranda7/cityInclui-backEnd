package com.gtp.cityinclui.exception;

import com.gtp.cityinclui.errorDTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> emailAlreadyExistsHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> webExchangeBindHandlerException(WebExchangeBindException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<String> erros = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String errorMessage = error.getDefaultMessage();
                    return errorMessage;
                })
                .collect(Collectors.toList());

        String finalErrorMessage = String.join(", ",erros);

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                finalErrorMessage,
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

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> invalidCredentialsHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );

    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> userNotFoundHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()

        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> authenticationRequiredHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(MissingDataException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> missingDataHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(InvalidFileFormatException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> invalidFileFormatHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(PhotoLimitExceededException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> photoLimitExceededHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(PhotoRequiredException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> photoRequiredHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> reviewAlreadyExistsHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> reviewNotFoundHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(InvalidUserException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> invalidUserHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }
}
