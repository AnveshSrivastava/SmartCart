package online.threadly.api_gateway.filter;

import online.threadly.api_gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/products")) {
            return chain.filter(exchange);
        }

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authorizationHeader.substring(7);
        if (!jwtService.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Derive X-USER-ID from the JWT and enforce it on outgoing request to
        // downstream services
        String userId = jwtService.extractUserId(token);

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> {
                    // Remove any client-sent X-USER-ID and set our trusted value
                    builder.headers(httpHeaders -> {
                        httpHeaders.remove("X-USER-ID");
                        if (userId != null) {
                            httpHeaders.add("X-USER-ID", userId);
                        }
                    });
                })
                .build();

        return chain.filter(mutatedExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
