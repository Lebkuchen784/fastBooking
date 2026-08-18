package com.project.bookingService.config;

import com.project.bookingService.config.authentication.JWTAuthFilter;
import com.project.bookingService.config.authentication.JWTUtility;
import com.project.bookingService.user.businessOwner.OwnerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationProvider authenticationProvider(OwnerService ownerService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(ownerService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OwnerService ownerService,
            JWTUtility jwtUtility,
            AuthenticationProvider authenticationProvider
    ){
        JWTAuthFilter jwtAuthFilter = new JWTAuthFilter(ownerService, jwtUtility);

        return http
                   .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                   .csrf(AbstractHttpConfigurer::disable)
                   .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/styles.css", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/register").permitAll()
                        .requestMatchers("/owners/register", "/owners/generateToken").permitAll()
                        .requestMatchers("/organizations/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/client").permitAll()
                        .anyRequest().authenticated()
                )
                   .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authenticationProvider(authenticationProvider)
                   .logout(logout -> logout.logoutSuccessUrl("/").deleteCookies("jwtToken"))
                   .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
