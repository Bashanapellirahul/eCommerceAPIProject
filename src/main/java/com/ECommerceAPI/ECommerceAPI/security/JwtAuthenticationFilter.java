package com.ECommerceAPI.ECommerceAPI.security;

import com.ECommerceAPI.ECommerceAPI.user.model.UserEntity;
import com.ECommerceAPI.ECommerceAPI.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 🔥 STEP 1: Skip auth endpoints
        String path = request.getServletPath();

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 STEP 2: Get Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 STEP 3: Extract token
        String token = authHeader.substring(7);

        try {
            // 🔥 STEP 4: Validate token
            if (jwtUtil.validateToken(token)) {

                // 🔥 STEP 5: Extract username
                String username = jwtUtil.extractUsername(token);

                // 🔥 STEP 6: Load user from DB
                UserEntity user = userRepository.findByUserName(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // 🔥 STEP 7: Extract roles and set authorities
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(user.getRole().name()));

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                authorities
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 🔥 STEP 8: Set authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            System.out.println("JWT Error: " + e.getMessage());
        }

        // 🔥 STEP 9: Continue request
        filterChain.doFilter(request, response);
    }
}