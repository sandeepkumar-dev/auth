package com.auth.config;


import com.auth.service.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigFile {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        request -> {
//                            request.requestMatchers("/api/v1/auth/patient_signup","/api/v1/auth/doctor_signup","/api/v1/auth/login").permitAll() OR Second Line we can use
                            request.requestMatchers("/api/v1/auth/**","/api/v1/welcome/hello").permitAll()
                                   // .requestMatchers("/api/v1/welcome/hello").hasAnyRole("PATIENT","DOCTOR")
                                    .requestMatchers("/api/v1/welcome/hi").hasRole("DOCTOR")
                                    .anyRequest().authenticated();

                        }).httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomerUserDetailsService customerUserDetailsService,
            PasswordEncoder passwordEncoder

    ){
        DaoAuthenticationProvider authenticationProvider=
                new DaoAuthenticationProvider(customerUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return  authenticationProvider;

    }
}
