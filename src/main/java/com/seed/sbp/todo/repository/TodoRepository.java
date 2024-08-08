package com.seed.sbp.todo.repository;

import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 리스트(페이징)
    Page<Todo> findAllByCompletedAndUser(Boolean completed, User user, Pageable pageable);

    Optional<Todo> findByTodoSeqAndUser(Long todoSeq, User user);

    void deleteByTodoSeqAndUser(Long todoSeq, User user);
}
