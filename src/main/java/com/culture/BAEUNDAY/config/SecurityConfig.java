package com.culture.BAEUNDAY.config;

import com.culture.BAEUNDAY.domain.user.Role;
import com.culture.BAEUNDAY.jwt.Custom.JWTCheckFilter;
import com.culture.BAEUNDAY.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    @Bean //사용자 비밀번호 암호화
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //AuthenticationManager 동적 생성을 위해 필요
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //disable 설정
        http
                .httpBasic((basic) -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .formLogin((auth) -> auth.disable());


        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/gpt").permitAll()
                .requestMatchers("/", "/login", "/logout", "/user/login", "/user/logout", "/user/check-name", "/user/check-username", "/user/register", "/reissue").permitAll()
                .requestMatchers("/user/profile/**", "/heart/**", "/review/**", "/posts/**", "/reserve/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .anyRequest().authenticated());


        http
            .addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //CORS 설정
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));

        return http.build();
    }

}

