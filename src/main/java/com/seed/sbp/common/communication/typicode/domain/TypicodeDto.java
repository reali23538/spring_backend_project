package com.seed.sbp.common.communication.typicode.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TypicodeDto {

    @Getter
    @Setter
    public static class Todo {
        @Schema(example = "1")
        private Long id;

        @Schema(example = "1")
        private Long userId;

        @Schema(example = "delectus aut autem")
        private String title;

        @Schema(example = "false")
        private Boolean completed;

        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }

    @Getter
    @Setter
    public static class AddTodo {
        @Schema(example = "1")
        private Long userId;

        @Schema(example = "delectus aut autem")
        private String title;

        @Schema(example = "false")
        private Boolean completed;
    }
}
