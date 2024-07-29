package com.seed.sbp.common.filter;

import com.seed.sbp.common.security.JwtProvider;
import com.seed.sbp.common.security.SbpUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final SbpUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
            String token = authorization.substring(7);

            // todo 이 부분에서 db 조회없이 토큰으로만 체크해도 될 듯
            // 토큰 검증
            if (token != null && jwtProvider.validateToken(token)) {
                // 유저 확인
                Claims claims = jwtProvider.getClaims(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

                // 현재 request의 Security Context에 추가 (인가 성공하여 승인된 request라는 의미)
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
