package com.taskmaster.config;

import com.taskmaster.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

               .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/login", "/users/register")
                .permitAll()
                .anyRequest()
                .authenticated()

                /*.authorizeHttpRequests(auth -> auth
                .anyRequest()
                .permitAll()*/
                
            )



            .addFilterBefore(jwtFilter,
                    UsernamePasswordAuthenticationFilter.class)

            .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}