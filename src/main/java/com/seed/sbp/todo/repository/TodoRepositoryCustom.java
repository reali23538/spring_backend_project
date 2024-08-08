package com.seed.sbp.todo.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seed.sbp.todo.domain.QTodo;
import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Todo> findAllBySearch(TodoDto.TodoSearch search) {
        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> q = queryFactory.selectFrom(todo)
                .where(
                        todo.completed.eq(search.getCompleted()),
                        todo.user.userSeq.eq(search.getUser().getUserSeq())
                );
        return q.fetch();
    }
}
