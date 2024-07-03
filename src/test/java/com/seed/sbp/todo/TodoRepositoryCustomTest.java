package com.seed.sbp.todo;

import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.repository.TodoRepository;
import com.seed.sbp.todo.repository.TodoRepositoryCustom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TodoRepositoryCustomTest {

    @Autowired
    private TodoRepositoryCustom todoRepositoryCustom;

    @Autowired
    private TodoRepository todoRepository;

    @DisplayName("할일 목록 조회 (custom)")
    @Test
    void findAllBySearch() throws ParseException {
        // given
        todoRepository.save(getTodo());
        TodoDto.TodoSearch search = new TodoDto.TodoSearch();
        search.setCompleted(false);

        // when
        List<Todo> todos = todoRepositoryCustom.findAllBySearch(search);
        System.out.println("todos size >> " + todos.size());

        // then
        assertThat(todos.size()).isEqualTo(4);
    }

    private Todo getTodo() throws ParseException {
        Todo todo = new Todo();
        todo.setTitle("할일1");
        todo.setCompleted(false);
        todo.setRegDate("202407021800");
        return todo;
    }

}
