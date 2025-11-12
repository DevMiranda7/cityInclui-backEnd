package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.auth.AuthRequestDTO;
import com.gtp.cityinclui.dto.auth.AuthResponseDTO;
import com.gtp.cityinclui.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/owner/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthResponseDTO> loginOwner(@RequestBody @Valid AuthRequestDTO request){
        return authService.loginOwner(request);
    }

    @PostMapping("/client/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthResponseDTO> loginClient(@RequestBody @Valid AuthRequestDTO request){
        return authService.loginClient(request);
    }
}
