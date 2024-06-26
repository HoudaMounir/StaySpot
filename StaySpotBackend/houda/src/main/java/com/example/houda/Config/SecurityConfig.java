package com.example.houda.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private  final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth-> auth.requestMatchers("/api/v1/auth/**").permitAll())
                .authorizeHttpRequests(auth-> auth.requestMatchers("/ws/**").permitAll())
                .authorizeHttpRequests(auth-> auth.requestMatchers("/properties/**").permitAll())
                .authorizeHttpRequests(auth->auth.anyRequest().authenticated())

                .httpBasic(Customizer.withDefaults())
                .sessionManagement(auth->auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);
                


        return http.build();
    }

    @Bean
    public String folderPath() {
        return "C:/Users/pro/Documents/Projet_SpringBoot/images/";
    }
}
