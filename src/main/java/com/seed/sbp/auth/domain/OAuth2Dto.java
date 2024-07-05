package com.seed.sbp.auth.domain;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class OAuth2Dto {

    @Builder
    @Getter
    public static class UserInfo {

        private String id;

        private String email;

        private String nickName;

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }

    }

}
