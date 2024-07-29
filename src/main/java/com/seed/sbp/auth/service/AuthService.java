package com.seed.sbp.auth.service;

import com.seed.sbp.auth.domain.AuthDto;
import com.seed.sbp.common.exception.SbpException;
import com.seed.sbp.common.response.CommonResultCode;
import com.seed.sbp.common.security.JwtProvider;
import com.seed.sbp.user.domain.User;
import com.seed.sbp.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthDto.LoginResult login(AuthDto.LoginUser loginUser) throws SbpException {
        log.debug("encrypt password >>> {}", passwordEncoder.encode(loginUser.getPassword()));

        // todo 아래 방법 대신. 이 부분에서 시큐리티로 인증하고 > 필터에서 DB 조회 없이 토큰으로만 체크해도 될 듯
        // 이메일, 패스워드 체크
        User user = userRepository.findByEmail(loginUser.getEmail())
                .orElseThrow(() -> new SbpException(CommonResultCode.NOT_FOUND_USER));
        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            throw new SbpException(CommonResultCode.WRONG_ID_OR_PASSWORD);
        }

        // 토큰 생성
        AuthDto.LoginResult loginResult = jwtProvider.createToken(user.getEmail(), user.getRoles());
        user.setRefreshToken(loginResult.getRefreshToken());
        userRepository.save(user);
        return loginResult;
    }

    public AuthDto.LoginResult refresh(AuthDto.TokenInfo tokenInfo) throws SbpException {
        // 리프레시 토큰 검증
        String refreshToken = tokenInfo.getRefreshToken();
        if (!jwtProvider.validateToken(refreshToken)) throw new SbpException(CommonResultCode.NOT_VALID);

        // 리프레시 토큰 확인
        Claims claims = jwtProvider.getClaims(refreshToken);
        User user = userRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new SbpException(CommonResultCode.NOT_FOUND_USER));
        if (!user.getRefreshToken().equals(refreshToken)) throw new SbpException(CommonResultCode.WRONG_REFRESH_TOKEN);

        // 토큰 생성
        AuthDto.LoginResult loginResult = jwtProvider.createToken(user.getEmail(), user.getRoles());
        user.setRefreshToken(loginResult.getRefreshToken());
        userRepository.save(user);
        return loginResult;
    }
}
