package com.seed.sbp.user.domain;

import com.seed.sbp.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    private Integer loginFailCnt;

    private String lockYn;

    private String refreshToken;

    @Transient
    private List<String> roles = List.of("ROLE_MEMBER"); // role table 있는 경우, 수정 필요

    @OneToMany(mappedBy = "user")
    private List<Todo> todos = new ArrayList<>();

}
