package com.seed.sbp.auth.web;

import com.seed.sbp.auth.domain.AuthDto;
import com.seed.sbp.auth.service.AuthService;
import com.seed.sbp.auth.service.OAuth2LoginService;
import com.seed.sbp.common.exception.SbpException;
import com.seed.sbp.common.response.CommonResult;
import com.seed.sbp.common.response.ResponseProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Auth API 입니다.")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;
    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping("/login")
    public ResponseEntity<CommonResult<AuthDto.LoginResult>> login(@RequestBody AuthDto.LoginUser loginUser) {
        try {
            AuthDto.LoginResult rst = authService.login(loginUser);
            return ResponseProvider.ok(rst);
        } catch (SbpException e) {
            return ResponseProvider.fail(e.getResultCode());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<CommonResult<AuthDto.LoginResult>> refresh(@RequestBody AuthDto.TokenInfo tokenInfo) {
        try {
            AuthDto.LoginResult rst = authService.refresh(tokenInfo);
            return ResponseProvider.ok(rst);
        } catch (SbpException e) {
            return ResponseProvider.fail(e.getResultCode());
        }
    }

    @GetMapping("/auth/{oAuth2Company}")
    public String oAuth2Login(@RequestParam(value = "code") String authCode, @PathVariable String oAuth2Company) {
        oAuth2LoginService.login(authCode, oAuth2Company);
        return "token 생성해서 리턴";
    }

}
