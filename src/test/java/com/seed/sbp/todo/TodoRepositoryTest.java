package com.seed.sbp.todo;

import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.repository.TodoRepository;
import com.seed.sbp.user.domain.User;
import com.seed.sbp.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
public class TodoRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @DisplayName("할일 등록 테스트")
    @Test
    void saveTest() throws ParseException {
        // given
        User user = getUser();
        User savedUser = userRepository.save(user);

        Todo todo = getTodo(savedUser);

        // when
        Todo savedTodo = todoRepository.save(todo);
        log.debug("savedTodo todoSeq : {}", savedTodo.getTodoSeq());

        // then
        assertThat(savedTodo.getTitle()).isEqualTo(todo.getTitle());
        assertThat(savedTodo.getUser().getUserSeq()).isEqualTo(savedUser.getUserSeq());
    }

    @DisplayName("할일 목록 조회 테스트 (특정유저)")
    @Test
    void findAllByCompletedAndUserTest() throws ParseException {
        // given
        User user = getUser();
        User savedUser = userRepository.save(user);

        Todo todo = getTodo(savedUser);
        Todo savedTodo = todoRepository.save(todo);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Todo> todoPage = todoRepository.findAllByCompletedAndUser(false, savedUser, pageable);
        List<Todo> todos = todoPage.getContent();
        log.debug("todos size : {}", todos.size());

        // then
        assertThat(todos.size()).isEqualTo(1);
    }

    @DisplayName("할일 상세 조회 테스트")
    @Test
    void findByTodoSeqAndUserTest() throws ParseException {
        // given
        User user = getUser();
        User savedUser = userRepository.save(user);

        Todo todo = getTodo(savedUser);
        Todo savedTodo = todoRepository.save(todo);

        // when
        Optional<Todo> oTodo = todoRepository.findByTodoSeqAndUser(savedTodo.getTodoSeq(), savedUser);
        log.debug("oTodo isPresent : {}", oTodo.isPresent());
        Todo rTodo = oTodo.get();

        // then
        assertThat(oTodo.isPresent()).isTrue();
        assertThat(rTodo.getTitle()).isEqualTo(todo.getTitle());
        assertThat(rTodo.getUser().getUserSeq()).isEqualTo(savedUser.getUserSeq());
    }

    @DisplayName("할일 삭제 테스트")
    @Test
    void deleteByTodoSeqAndUserTest() throws ParseException {
        // given
        User user = getUser();
        User savedUser = userRepository.save(user);

        Todo todo = getTodo(savedUser);
        Todo savedTodo = todoRepository.save(todo);
        log.debug("savedTodo todoSeq : {}", savedTodo.getTodoSeq());

        // when
        todoRepository.deleteByTodoSeqAndUser(savedTodo.getTodoSeq(), savedTodo.getUser());

        // then
        Optional<Todo> oTodo = todoRepository.findById(savedTodo.getTodoSeq());
        log.debug("oTodo isPresent : {}", oTodo.isPresent());
        assertThat(oTodo.isPresent()).isFalse();
    }

    private User getUser() {
        Date now = new Date();
        String encPassword = passwordEncoder.encode("pppp");

        User user = new User();
        user.setEmail("sbpunit@sbp.com");
        user.setNickname("sbp");
        user.setPassword(encPassword);
        user.setGoogleYn("N");
        user.setRegDate(now);
        user.setLastLoginDate(now);
        user.setLoginFailCnt(0);
        user.setLockYn("N");
        user.setRefreshToken(null);
        return user;
    }
    private Todo getTodo(User user) throws ParseException {
        Todo todo = new Todo();
        todo.setTitle("할일1");
        todo.setCompleted(false);
        todo.setUser(user);
        todo.setRegDate("202408211427");
        return todo;
    }

}
