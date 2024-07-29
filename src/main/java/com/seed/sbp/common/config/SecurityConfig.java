package com.seed.sbp.common.config;

import com.seed.sbp.common.filter.AuthFilter;
import com.seed.sbp.common.security.JwtProvider;
import com.seed.sbp.common.security.SbpUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final SbpUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
                .httpBasic(HttpBasicConfigurer::disable)
                // 쿠키 기반이 아닌 JWT 기반이므로 사용하지 않음
                .csrf(AbstractHttpConfigurer::disable)
                // Spring Security 세션 정책 : JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 조건별로 요청 허용/제한 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/swagger-ui/**", "/v3/api-docs/**", // 스웨거
                                "/login", "/refresh" // 로그인, 토큰 재발급은 모두 승인
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin으로 시작하는 요청은 ADMIN 권한이 있는 유저에게만 허용
                        .requestMatchers("/todos/**").hasRole("MEMBER") // /todos 로 시작하는 요청은 MEMBER 권한이 있는 유저에게만 허용
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터 적용. AuthFilter > UsernamePasswordAuthenticationFilter 순으로 진행
                // Token이 유효한지 체크 => 유효하다면 UsernamePasswordAuthenticationFilter를 통과할 수 있게끔 권한 부여
                .addFilterBefore(new AuthFilter(jwtProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class)

                // todo 에러 핸들링
                .exceptionHandling(authenticationManager -> authenticationManager
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                // 권한 문제가 발생했을 때 이 부분을 호출한다.
                                response.setStatus(403);
                                response.setCharacterEncoding("utf-8");
                                response.setContentType("text/html; charset=UTF-8");
                                response.getWriter().write("권한이 없는 사용자입니다.");
                            }
                        })
//                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                            @Override
//                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//                                // 인증문제가 발생했을 때 이 부분을 호출한다.
//                                response.setStatus(401);
//                                response.setCharacterEncoding("utf-8");
//                                response.setContentType("text/html; charset=UTF-8");
//                                response.getWriter().write("인증되지 않은 사용자입니다.");
//                            }
//                        })
                );
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
