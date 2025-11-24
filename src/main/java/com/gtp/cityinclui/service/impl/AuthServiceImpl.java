package com.gtp.cityinclui.service.impl;

import com.gtp.cityinclui.dto.auth.AuthRequestDTO;
import com.gtp.cityinclui.dto.auth.AuthResponseDTO;
import com.gtp.cityinclui.exception.InvalidCredentialsException;
import com.gtp.cityinclui.repository.CustomerRepository;
import com.gtp.cityinclui.repository.RestaurantOwnerRepository;
import com.gtp.cityinclui.security.JwtUtil;
import com.gtp.cityinclui.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {

    private final RestaurantOwnerRepository restaurantOwnerRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(RestaurantOwnerRepository restaurantOwnerRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.restaurantOwnerRepository = restaurantOwnerRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Mono<AuthResponseDTO> login(AuthRequestDTO request) {
        String userType = request.getUserType();

        if ("OWNER".equalsIgnoreCase(userType)){
            return this.loginOwner(request);
        } else if ("CLIENT".equalsIgnoreCase(userType)) {
            return this.loginClient(request);
        }else {
            return Mono.error(new InvalidCredentialsException("Tipo de usuário inválido"));
        }
    }

    private Mono<AuthResponseDTO> loginOwner(AuthRequestDTO request) {
        return restaurantOwnerRepository.findByEmail(request.getEmail())
                .flatMap(owner -> {
                    if (passwordEncoder.matches(request.getSenha(), owner.getSenha())){

                        String token = jwtUtil.generateToken(owner.getEmail(), "ROLE_OWNER");
                        return Mono.just(new AuthResponseDTO(token));
                    }else{
                        return Mono.error(new InvalidCredentialsException("E-mail ou senha inválidos"));
                    }
                }).switchIfEmpty(Mono.error(new InvalidCredentialsException("E-mail ou senha inválidos")));
    }


    private Mono<AuthResponseDTO> loginClient(AuthRequestDTO request) {
        return customerRepository.findByEmail(request.getEmail())
                .flatMap(client -> {
                    if (passwordEncoder.matches(request.getSenha(), client.getSenha())){
                        String token = jwtUtil.generateToken(client.getEmail(),"ROLE_CLIENT");
                        return Mono.just(new AuthResponseDTO(token));
                    }else{
                        return Mono.error(new InvalidCredentialsException("E-mail ou senha inválidos"));
                    }
                }).switchIfEmpty(Mono.error(new InvalidCredentialsException("E-mail ou senha inválidos")));
    }
}
