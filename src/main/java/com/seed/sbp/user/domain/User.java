package com.seed.sbp.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String email;

    private String nickname;

    private String password;

    private String googleYn;

    private Date regDate;

    private Date lastLoginDate;

    private Integer loginFailCnt;

    private String lockYn;

    private String refreshToken;

    @Transient
    private List<String> roles = List.of("ROLE_MEMBER"); // todo role table 에서 가져오기

}
