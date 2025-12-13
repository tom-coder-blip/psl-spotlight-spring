package net.javaguides.pslspotlightspring.config;

import net.javaguides.pslspotlightspring.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import net.javaguides.pslspotlightspring.security.JwtAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/uploads/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/players/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/players/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/players/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/players/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/likes/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/likes/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/likes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/notifications/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/search/**").permitAll()
                        // finally, catch-all
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}