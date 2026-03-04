package com.auth.filter;

import com.auth.service.CustomerUserDetailsService;
import com.auth.service.JwtService;
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

@Component
public class JWTFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private CustomerUserDetailsService userDetailsService;

    public JWTFilter(JwtService jwtService, CustomerUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        
        if(token!=null && token.startsWith("Bearer ")){
            String authToken = token.substring(7);
            String username = jwtService.validateTokenAndRetrieveSubject(authToken);
            if(username != null){
                var userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("Authorities: " + userDetails.getAuthorities());
                var auth  = new UsernamePasswordAuthenticationToken(
                        userDetails, null,userDetails.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }
        filterChain.doFilter(request, response);


    }
}
