package com.seed.sbp.todo;

import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.repository.TodoRepository;
import com.seed.sbp.todo.repository.TodoRepositoryCustom;
import com.seed.sbp.user.domain.User;
import com.seed.sbp.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/*
TodoRepositoryCustom을 테스트 하는데
단위 테스트로 하면 문제가 있어서
일단 통합 테스트로 진행
 */
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
public class TodoRepositoryCustomTest {

    @Autowired
    private TodoRepositoryCustom todoRepositoryCustom;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("할일 목록 조회 (custom)")
    @Test
    void findAllBySearch() throws ParseException {
        // given
        // user 저장
        User user = getUser();
        User savedUser = userRepository.save(user);
        System.out.println("savedUser userSeq >>> " + savedUser.getUserSeq());
        // todo_저장
        Todo todo = getTodo(savedUser);
        Todo savedTodo = todoRepository.save(todo);
        System.out.println("savedTodo todoSeq >>> " + savedTodo.getTodoSeq());
        // 검색 세팅
        TodoDto.User searchUser = new TodoDto.User(savedUser.getUserSeq());
        TodoDto.TodoSearch search = new TodoDto.TodoSearch();
        search.setCompleted(false);
        search.setUser(searchUser);

        // when
        List<Todo> todos = todoRepositoryCustom.findAllBySearch(search);
        System.out.println("todos size >> " + todos.size());

        // then
//        assertThat(todos.size()).isEqualTo(1);
    }

    private User getUser() {
        User user = new User();
        user.setEmail("sbp@sbp.net");
        user.setGoogleYn("N");
        user.setRegDate(new Date());
        user.setLastLoginDate(new Date());
        user.setLoginFailCnt(0);
        user.setLockYn("N");
        return user;
    }

    private Todo getTodo(User user) throws ParseException {
        Todo todo = new Todo();
        todo.setTitle("할일1");
        todo.setCompleted(false);
        todo.setRegDate("202407021800");
        todo.setUser(user);
        return todo;
    }

}
