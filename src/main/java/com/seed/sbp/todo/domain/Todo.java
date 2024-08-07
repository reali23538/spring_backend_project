package com.seed.sbp.todo.domain;

import com.seed.sbp.common.util.DateUtil;
import com.seed.sbp.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoSeq;

    private String title;

    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    public String getStrRegDate() {
        return DateUtil.dateToString(this.regDate);
    }

    public void setRegDate(String regDate) throws ParseException {
        this.regDate = DateUtil.stringToDate(regDate);
    }

}
