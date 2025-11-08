package com.gtp.cityinclui.exception;

import com.gtp.cityinclui.errorDTO.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaExistenteException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> emailJaExistenteHandlerException(RuntimeException ex){

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
    public Mono<ResponseEntity<ErrorResponseDTO>> webExchangeBindHandlerExcepion(WebExchangeBindException ex){

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
    public Mono<ResponseEntity<ErrorResponseDTO>> InvalidCredentialsHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
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

    @ExceptionHandler(AutenticacaoNecessariaException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> AutenticacaoNecessariaHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(DadosEmFaltaException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> DadosEmFaltaHandlerException(RuntimeException ex){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO, status)
        );
    }

    @ExceptionHandler(FormatoArquivoInvalidoException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> FormatoArquivoInvalidoHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(LimiteDeFotosExcedidoException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> LimiteDeFotosExcedidoHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

    @ExceptionHandler(FotoNecessariaException.class)
    public Mono<ResponseEntity<ErrorResponseDTO>> FotoNecessariaHandlerException(RuntimeException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                ex.getMessage(),
                status.value()
        );
        return Mono.just(
                new ResponseEntity<>(errorResponseDTO,status)
        );
    }

}
