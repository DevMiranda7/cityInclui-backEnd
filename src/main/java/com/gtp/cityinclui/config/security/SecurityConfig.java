package com.gtp.cityinclui.config.security;

import com.gtp.cityinclui.security.JwtAuthenticationManager;
import com.gtp.cityinclui.security.JwtSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtSecurityContextRepository jwtSecurityContextRepository;

    public SecurityConfig(JwtAuthenticationManager jwtAuthenticationManager, JwtSecurityContextRepository jwtSecurityContextRepository){
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.jwtSecurityContextRepository = jwtSecurityContextRepository;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(jwtAuthenticationManager)
                .securityContextRepository(jwtSecurityContextRepository)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((exchange, ex) -> Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                        )
                        .accessDeniedHandler((exchange, denied) ->
                                Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                        )
                )
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers(HttpMethod.POST,"/cityinclui/cadastrar-anunciante").permitAll()
                        .pathMatchers(HttpMethod.GET,"/cityinclui/restaurante/{ownerId}").permitAll()
                        .pathMatchers(HttpMethod.GET,"/cityinclui/restaurantes/**").permitAll()
                        .pathMatchers(HttpMethod.GET,"/cityinclui/perfil-anunciante").hasRole("OWNER")
                        .pathMatchers(HttpMethod.PUT,"/cityinclui/editar-perfil").hasRole("OWNER")
                        .pathMatchers(HttpMethod.DELETE,"/cityinclui/delete-conta").hasRole("OWNER")

                        .pathMatchers(HttpMethod.POST,"/cityinclui/cadastrar-cliente").permitAll()
                        .pathMatchers(HttpMethod.GET, "/cityinclui/exibir-clientes/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/cityinclui/cliente/me").hasRole("CLIENT")
                        .pathMatchers(HttpMethod.PUT,"/cityinclui/editar-cliente").hasRole("CLIENT")
                        .pathMatchers(HttpMethod.DELETE,"/cityinclui/delete-conta").hasRole("CLIENT")

                        .pathMatchers(HttpMethod.POST, "/cityinclui/owner/*/avaliacoes").hasRole("CLIENT")
                        .pathMatchers(HttpMethod.GET, "/cityinclui/owner/*/avaliacoes").permitAll()
                        .pathMatchers(HttpMethod.PUT, "/cityinclui/owner/*/avaliacoes/**").hasRole("CLIENT")
                        .pathMatchers(HttpMethod.DELETE, "/cityinclui/owner/*/avaliacoes/**").hasRole("CLIENT")
                        .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

}
