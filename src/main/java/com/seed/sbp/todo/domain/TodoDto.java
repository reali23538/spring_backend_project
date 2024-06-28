package com.seed.sbp.todo.domain;

import com.seed.sbp.common.domain.Search;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @Schema(description = "할일 리스트")
        private List<Todo> todos;

        @Schema(description = "할일 총개수", example = "1")
        private Long totalRowCnt;

        @Schema(description = "페이지당 row의 개수", example = "10")
        private Integer rowCntPerPage;
    }

    @Builder
    @Getter
    @Setter
    public static class Todo {
        @Schema(description = "할일 seq (등록시에는 null로 세팅)", example = "1")
        private Long todoSeq;

        @NotBlank(message = "할일을 입력해주세요.")
        @Schema(description = "할일", example = "청소")
        private String title;

        @NotNull
        @Schema(description = "완료여부", example = "false")
        private Boolean completed;

        @NotBlank(message = "등록일시를 입력해주세요.")
        @Schema(description = "등록일시 (yyyyMMddHHmm)", example = "202405282010")
        private String regDate;
    }

}
