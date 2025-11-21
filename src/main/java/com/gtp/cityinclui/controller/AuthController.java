package com.gtp.cityinclui.controller;

import com.gtp.cityinclui.dto.auth.AuthRequestDTO;
import com.gtp.cityinclui.dto.auth.AuthResponseDTO;
import com.gtp.cityinclui.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponseDTO>> login(@RequestBody @Valid AuthRequestDTO request){
        return authService.login(request)
                .map(authResponse -> {
                    ResponseCookie cookie = ResponseCookie.from("auth_token", authResponse.getToken())
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(24 * 60 * 60)
                            .sameSite("Strict")
                            .build();

                    return ResponseEntity.ok()
                            .header("Set-Cookie", cookie.toString())
                            .body(authResponse);
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(){
                    ResponseCookie cookie = ResponseCookie.from("auth_token", "")
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(0)
                            .sameSite("Strict")
                            .build();

                    return Mono.just(ResponseEntity.ok()
                            .header("Set-Cookie", cookie.toString())
                            .build());
    }

}
