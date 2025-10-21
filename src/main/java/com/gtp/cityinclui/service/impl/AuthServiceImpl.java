package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.auth.AuthRequestDTO;
import com.gtp.cityinclui.dto.auth.AuthResponseDTO;
import com.gtp.cityinclui.repository.OwnerRepository;
import com.gtp.cityinclui.security.JwtUtil;
import com.gtp.cityinclui.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException() {
            super("Email ou senha inválidos.");
        }
    }

    public AuthServiceImpl(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<AuthResponseDTO> loginOwner(AuthRequestDTO request) {
        return ownerRepository.findByEmail(request.getEmail())
                .flatMap(owner -> {
                    if (passwordEncoder.matches(request.getSenha(), owner.getSenha())){

                        String token = jwtUtil.generateToken(owner.getEmail(), "ROLE_OWNER");
                        return Mono.just(new AuthResponseDTO(token));
                    }else{
                        return Mono.error(new InvalidCredentialsException());
                    }
                }).switchIfEmpty(Mono.error(new InvalidCredentialsException()));
    }
}
