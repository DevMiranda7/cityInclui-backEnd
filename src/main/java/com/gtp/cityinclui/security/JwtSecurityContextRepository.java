package com.gtp.cityinclui.security;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class JwtSecurityContextRepository  implements ServerSecurityContextRepository {
    private final JwtAuthenticationManager jwtAuthenticationManager;
    private static final String TOKEN_PREFIX = "Bearer ";
    public JwtSecurityContextRepository(JwtAuthenticationManager jwtAuthenticationManager){
        this.jwtAuthenticationManager = jwtAuthenticationManager;
    }
    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)){
            String authToken = authHeader.substring(TOKEN_PREFIX.length());

            Authentication authentication = new UsernamePasswordAuthenticationToken(authToken,authToken);

            return this.jwtAuthenticationManager.authenticate(authentication)
                    .map(SecurityContextImpl::new);
        }else{
            return Mono.empty();
        }
    }
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }
}