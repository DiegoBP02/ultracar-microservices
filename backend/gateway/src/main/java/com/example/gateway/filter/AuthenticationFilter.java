package com.example.gateway.filter;

import com.example.gateway.exceptions.InvalidTokenException;
import com.example.gateway.exceptions.MissingAuthorizationHeader;
import com.example.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new MissingAuthorizationHeader(HttpStatus.BAD_REQUEST, "Missing authorization header");
                }

                String authHeader = exchange.getRequest()
                        .getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }

                try{
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e){
                    throw new InvalidTokenException(HttpStatus.BAD_REQUEST, e.getMessage());
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }

}
