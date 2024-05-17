package com.seed.sbp.todo.domain;

import com.seed.sbp.common.domain.Search;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TodoDto {

    @Getter
    @Setter
    public static class TodoSearch extends Search {
        private Boolean completed = false;
    }

    @Builder
    @Getter
    public static class TodoPage {
        private List<Todo> todos;

        private Long totalRowCnt;

        private Integer rowCntPerPage;
    }

    @Builder
    @Getter
    @Setter
    public static class Todo {
        private Long todoSeq;

        private String title;

        private Boolean completed;

        private String regDate;
    }

}
