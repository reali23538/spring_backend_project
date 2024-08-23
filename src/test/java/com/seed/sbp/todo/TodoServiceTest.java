package com.seed.sbp.todo;

import com.seed.sbp.common.exception.SbpException;
import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.repository.TodoRepository;
import com.seed.sbp.todo.service.TodoService;
import com.seed.sbp.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Spy
    private ModelMapper modelMapper;

    @DisplayName("할일 목록 조회 테스트 (특정유저)")
    @Test
    void getTodoPageTest() throws ParseException {
        // given
        TodoDto.TodoSearch search = new TodoDto.TodoSearch();
        search.setUser(new TodoDto.User(1L));
        search.setOrderBy("todoSeq");

        Page<Todo> todoPage = new PageImpl(getTodos());
        doReturn(todoPage)
                .when(todoRepository)
                .findAllByCompletedAndUser(anyBoolean(), any(User.class), any(PageRequest.class));

        // when
        TodoDto.TodoPage rstTodoPage = todoService.getTodoPage(search);
        System.out.println("todos size : " + rstTodoPage.getTodos().size());
        System.out.println("totalRowCnt : " + rstTodoPage.getTotalRowCnt());

        // then
        assertThat(rstTodoPage.getTodos().size()).isEqualTo(10);
        // verify
        verify(todoRepository, times(1)).findAllByCompletedAndUser(any(Boolean.class), any(User.class), any(PageRequest.class));
    }

    @DisplayName("할일 상세 조회 테스트")
    @Test
    void getTodoTest() throws ParseException, SbpException {
        // given
        Todo todo = getTodo(1L);
        doReturn(Optional.of(todo))
                .when(todoRepository)
                .findByTodoSeqAndUser(anyLong(), any(User.class));

        // when
        TodoDto.Todo rstTodo = todoService.getTodo(1L, 1L);
        System.out.println("todoSeq : " + rstTodo.getTodoSeq());
        System.out.println("title : " + rstTodo.getTitle());

        // then
        assertThat(rstTodo.getTodoSeq()).isEqualTo(todo.getTodoSeq());
        assertThat(rstTodo.getTitle()).isEqualTo(todo.getTitle());
        // verify
        verify(todoRepository, times(1)).findByTodoSeqAndUser(anyLong(), any(User.class));
    }

    private List<Todo> getTodos() throws ParseException {
        List<Todo> todos = new ArrayList<>();

        for (long i=1; i<=10; i++) {
            Todo todo = getTodo(i);
            todos.add(todo);
        }
        return todos;
    }
    private Todo getTodo(long i) throws ParseException {
        User user = new User();
        user.setUserSeq(1L);

        Todo todo = new Todo();
        todo.setTodoSeq(i);
        todo.setTitle("할일" + i);
        todo.setCompleted(false);
        todo.setUser(user);
        todo.setRegDate("202408211427");
        return todo;
    }

    @DisplayName("할일 등록 테스트")
    @Test
    void addTest() throws ParseException {
        // given
        TodoDto.Todo reqTodo = new TodoDto.Todo();
        reqTodo.setTitle("할일1");
        reqTodo.setCompleted(false);
        reqTodo.setRegDate("202408231900");
        reqTodo.setUser(new TodoDto.User(1L));

        User user = new User();
        user.setUserSeq(1L);
        Todo savedTodo = new Todo();
        savedTodo.setTodoSeq(1L);
        savedTodo.setTitle("할일1");
        savedTodo.setCompleted(false);
        savedTodo.setUser(user);
        savedTodo.setRegDate("202408231900");

        doReturn(savedTodo)
                .when(todoRepository)
                .save(any(Todo.class));

        // when
        TodoDto.Todo rstTodo = todoService.add(reqTodo);
        System.out.println("rstTodo todoSeq : " + rstTodo.getTodoSeq());

        // then
        assertThat(rstTodo.getTodoSeq()).isEqualTo(1L);

        // verify
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @DisplayName("할일 수정 테스트")
    @Test
    void modifyTest() throws Exception {
        // given
        // request
        TodoDto.Todo reqTodo = new TodoDto.Todo();
        reqTodo.setTodoSeq(1L);
        reqTodo.setTitle("할일2");
        reqTodo.setCompleted(false);
        reqTodo.setRegDate("202408231910");
        reqTodo.setUser(new TodoDto.User(1L));

        User user = new User();
        user.setUserSeq(1L);
        // saved
        Todo savedTodo = new Todo();
        savedTodo.setTodoSeq(1L);
        savedTodo.setTitle("할일1");
        savedTodo.setCompleted(false);
        savedTodo.setUser(user);
        savedTodo.setRegDate("202408231900");
        doReturn(Optional.of(savedTodo))
                .when(todoRepository)
                .findByTodoSeqAndUser(anyLong(), any(User.class));
        // modified
        Todo modifiedTodo = new Todo();
        modifiedTodo.setTodoSeq(1L);
        modifiedTodo.setTitle("할일2");
        modifiedTodo.setCompleted(false);
        modifiedTodo.setUser(user);
        modifiedTodo.setRegDate("202408231910");
        doReturn(modifiedTodo)
                .when(todoRepository)
                .save(any(Todo.class));

        // when
        TodoDto.Todo rstTodo = todoService.modify(reqTodo);
        System.out.println("rstTodo todoSeq : " + rstTodo.getTodoSeq());

        // then
        assertThat(rstTodo.getTodoSeq()).isEqualTo(reqTodo.getTodoSeq());
        assertThat(rstTodo.getTitle()).isEqualTo(reqTodo.getTitle());

        // verify
        verify(todoRepository, times(1)).findByTodoSeqAndUser(anyLong(), any(User.class));
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @DisplayName("할일 삭제 테스트")
    @Test
    void removeTest() {
        // given
        Long todoSeq = 1L;
        Long userSeq = 1L;

        doNothing()
                .when(todoRepository)
                .deleteByTodoSeqAndUser(anyLong(), any(User.class));

        // when
        todoService.remove(todoSeq, userSeq);

        // verify
        verify(todoRepository, times(1)).deleteByTodoSeqAndUser(anyLong(), any(User.class));
    }

    @DisplayName("할일 상세 조회 실패 테스트")
    @Test
    void getTodoFailTest() {
        // given
        doReturn(Optional.empty())
                .when(todoRepository)
                .findByTodoSeqAndUser(anyLong(), any(User.class));

        // then
        assertThrows(SbpException.class, () -> todoService.getTodo(1L, 1L));
    }

}
