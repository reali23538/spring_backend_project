package com.seed.sbp.auth.domain;

import com.seed.sbp.common.security.AuthType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class AuthDto {

    @Getter
    public static class LoginUser {
        @Schema(description = "이메일", example = "sbp@sbp.com")
        private String email;

        @Schema(description = "패스워드", example = "1111")
        private String password;

        public LoginUser(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    @Setter
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    public static class LoginResult {
        private AuthType authType;
        private String accessToken;
        private String refreshToken;
    }
}
