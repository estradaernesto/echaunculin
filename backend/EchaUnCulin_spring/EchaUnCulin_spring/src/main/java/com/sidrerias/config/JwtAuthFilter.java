package com.sidrerias.config;

import com.sidrerias.repository.UsuarioRepository;
import com.sidrerias.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsuarioRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, UsuarioRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header->!header.isBlank())
                .map(header->header.substring(7))
                .map(jwtService::extractUserId)
                .flatMap(userId->userRepository.findById(Long.valueOf(userId)))
                .ifPresent(userDetails->{
                    request.setAttribute("X-user-Id", userDetails.getId());
                    processAuthentication(request,userDetails);
                });
        filterChain.doFilter(request, response);
    }

    private void processAuthentication(HttpServletRequest request, UserDetails userDetails) {
        String jwtToken=request.getHeader("Authorization").substring(7);
        Optional.of(jwtToken)
                .filter(token->!jwtService.isExpired(token))
                .ifPresent(token->{
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
    }
}
