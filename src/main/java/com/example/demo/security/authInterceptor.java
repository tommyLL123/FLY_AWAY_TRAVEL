package com.example.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class authInterceptor implements HandlerInterceptor {

    private final jwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        if (!isProtected(path)) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Map<String, String> claims = jwtService.validateToken(authHeader.substring(7));
        request.setAttribute("userId", Long.parseLong(claims.get("sub")));
        request.setAttribute("firstName", claims.get("firstName"));
        request.setAttribute("lastName", claims.get("lastName"));
        request.setAttribute("email", claims.get("email"));
        return true;
    }

    private boolean isProtected(String path) {
        return path.startsWith("/flights/search")
                || path.startsWith("/flights/book")
                || path.startsWith("/flight/book");
    }
}
