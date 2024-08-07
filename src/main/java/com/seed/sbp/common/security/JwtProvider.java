package com.seed.sbp.common.security;

import com.seed.sbp.auth.domain.AuthDto;
import com.seed.sbp.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private SecretKey secretKey;

    private final long ACCESS_EXP = 1000L * 60 * 60 * 2; // 2시간 - 만료시간(밀리세컨즈)
    private final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 7; // 7일
//    private final long ACCESS_EXP = 1000L * 60 * 1; // 1분
//    private final long REFRESH_EXP = 1000L * 60 * 5; // 5분

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public AuthDto.LoginResult createToken(User user) {
        Date now = new Date();
        Map claims = new HashMap();
        claims.put("userSeq", user.getUserSeq());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRoles().get(0));

        String accessToken = Jwts.builder()
//                .subject(id)
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ACCESS_EXP))
                .signWith(secretKey)
                .compact();
        String refreshToken = Jwts.builder()
                .subject(user.getEmail())
                .expiration(new Date(now.getTime() + REFRESH_EXP))
                .signWith(secretKey)
                .compact();

        return AuthDto.LoginResult.builder()
                .authType(AuthType.Bearer)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
