package com.gtp.cityinclui.service;

import com.gtp.cityinclui.dto.auth.AuthRequestDTO;
import com.gtp.cityinclui.dto.auth.AuthResponseDTO;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<AuthResponseDTO> loginOwner(AuthRequestDTO request);

    //Mono<AuthResponseDTO> loginClient(AuthRequestDTO request);
}
