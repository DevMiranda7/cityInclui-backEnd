package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.auth.AuthRequestDTO;
import com.gtp.cityinclui.dto.auth.AuthResponseDTO;
import com.gtp.cityinclui.service.AuthService;
import com.gtp.cityinclui.service.impl.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/owner/login")
    public Mono<ResponseEntity<AuthResponseDTO>> loginOwner(@RequestBody AuthRequestDTO request){
        return authService.loginOwner(request)
                .map(responseDTO -> ResponseEntity.ok().body(responseDTO))
                .onErrorResume(AuthServiceImpl.InvalidCredentialsException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(null))
                );
    }
}
