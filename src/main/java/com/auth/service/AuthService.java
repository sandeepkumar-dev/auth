package com.auth.service;

import com.auth.dto.APIResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    private UserRepository  userRepository;

    private PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public APIResponse<String> register (UserDto dto, String role) {

        if (userRepository.existsByUsername(dto.getUsername())){
            APIResponse<String > response= new APIResponse<>();
            response.setMessage("Registration failed");
            response.setStatus(500);
            response.setData("User with username exits");
            return  response;
        }

        if (userRepository.existsByEmail(dto.getEmail())){
            APIResponse<String > response= new APIResponse<>();
            response.setMessage("Registration failed");
            response.setStatus(500);
            response.setData("User with Email ld id exits");
            return response;
        }

        User user= new User();
        user.setRole(role);
        BeanUtils.copyProperties(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        APIResponse<String> response=new APIResponse<>();
        response.setMessage("Registration Successful");
        response.setStatus(201);
        response.setData("Transaction Completed");
        return response;
    }

}
