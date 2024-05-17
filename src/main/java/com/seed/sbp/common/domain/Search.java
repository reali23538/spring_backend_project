package com.seed.sbp.common.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class Search {
    private int currentPage = 1;

    private String orderBy;

    private Sort.Direction sortType = Sort.Direction.DESC;

    private int limit = 10;

    public int getCurrentPage() {
        return this.currentPage - 1;
    }
}
