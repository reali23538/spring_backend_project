package com.seed.sbp.todo.repository;

import com.seed.sbp.todo.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 리스트(페이징)
    Page<Todo> findAllByCompleted(Boolean completed, Pageable pageable);
}
