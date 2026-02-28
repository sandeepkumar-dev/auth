package com.auth.controller;


import com.auth.dto.APIResponse;
import com.auth.dto.LoginDto;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private AuthService authService;

    private AuthenticationManager authenticationManager;

    private JwtService jwtService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @PostMapping("/patient_signup")

    public ResponseEntity<APIResponse<String>> newPatient(
            @RequestBody UserDto userDto
    ) {
        APIResponse<String> response = authService.register(userDto, "ROLE_PATIENT");
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/doctor_signup")

    public ResponseEntity<APIResponse<String>> newDoctor(
            @RequestBody UserDto userDto
    ) {
        APIResponse<String> response = authService.register(userDto, "ROLE_DOCTOR");
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> loginCheck(@RequestBody LoginDto loginDto) {
        APIResponse<String> response = new APIResponse<>();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        if (authenticate.isAuthenticated()) {

            String jwtToken = jwtService.generateToken(loginDto.getUsername(), authenticate.getAuthorities().iterator().next().getAuthority());
            response.setData(jwtToken);
            response.setStatus(201);
            response.setMessage("Generated Token");
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus())) ;
        }
        response.setData("failed");
        response.setStatus(500);
        response.setMessage("Unable to authenticate");
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus())) ;
    }
}
