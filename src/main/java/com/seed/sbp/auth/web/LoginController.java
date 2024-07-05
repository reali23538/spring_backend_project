package com.seed.sbp.auth.web;

import com.seed.sbp.auth.service.OAuth2LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login API", description = "Login API 입니다.")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final OAuth2LoginService oAuth2LoginService;

    @GetMapping("/auth/{oAuth2Company}")
    public String oAuth2Login(@RequestParam(value = "code") String authCode, @PathVariable String oAuth2Company) {
        oAuth2LoginService.login(authCode, oAuth2Company);
        return "token 생성해서 리턴";
    }

}
