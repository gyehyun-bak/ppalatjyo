package ppalatjyo.server.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ppalatjyo.server.global.security.handler.CustomAccessDeniedHandler;
import ppalatjyo.server.global.security.handler.CustomAuthenticationEntryPoint;
import ppalatjyo.server.global.security.jwt.JwtAuthenticationFilter;
import ppalatjyo.server.global.security.jwt.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/favicon.ico",
                                "/error",
                                "/test/**",
                                "/docs/**",
                                "/api/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthenticationFilter(), LogoutFilter.class)
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                )
                .build();
    }

    /**
     * 주어진 AuthenticationProvider 리스트를 순회해 지원하는 인증 처리를 제공
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthenticationProvider);
    }

    /**
     * JWT 토큰으로 인증을 제공하는 필터
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authenticationManager());
    }
}
