package com.taskmanager.taskmanager.config.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.taskmanager.taskmanager.models.UserModel;
import com.taskmanager.taskmanager.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = this.tokenService.validateToken(token);

        if (login != null) {
            UserModel model = this.userService.findByEmail(login).orElseThrow(() -> new RuntimeException("User not found"));
            var authorities = Collections.singleton(new SimpleGrantedAuthority(model.getRole().toString()));
            var authorization = new UsernamePasswordAuthenticationToken(model, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authorization);
        }
        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest request) {
        var auth = request.getHeader("Authorization");
        if (auth == null) return null;
        return auth.replace("Bearer ", "");
    }
}
