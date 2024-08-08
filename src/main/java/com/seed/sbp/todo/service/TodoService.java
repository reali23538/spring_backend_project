package com.seed.sbp.todo.service;

import com.seed.sbp.common.exception.SbpException;
import com.seed.sbp.common.response.CommonResultCode;
import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.repository.TodoRepository;
import com.seed.sbp.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    private final ModelMapper modelMapper;

    // 리스트(페이징)
    public TodoDto.TodoPage getTodoPage(TodoDto.TodoSearch search) {
        User user = new User();
        user.setUserSeq(search.getUser().getUserSeq());
        PageRequest pageRequest = PageRequest.of(search.getCurrentPage(), search.getLimit(), search.getSortType(), search.getOrderBy());
        Page<Todo> todoPage = todoRepository.findAllByCompletedAndUser(search.getCompleted(), user, pageRequest);

        List<TodoDto.Todo> todos = todoPage.map(todo ->
                modelMapper.map(todo, TodoDto.Todo.class)).getContent();

        return TodoDto.TodoPage.builder()
                .todos(todos)
                .totalRowCnt(todoPage.getTotalElements())
                .rowCntPerPage(search.getLimit())
                .build();
    }

    // 상세
    public TodoDto.Todo getTodo(Long todoSeq, Long userSeq) throws SbpException {
        User user = new User();
        user.setUserSeq(userSeq);

        return todoRepository.findByTodoSeqAndUser(todoSeq, user)
                .map(todo -> modelMapper.map(todo, TodoDto.Todo.class))
                .orElseThrow(() -> new SbpException(CommonResultCode.COMMON_NO_CONTENT));
    }

    // 등록
    public TodoDto.Todo add(TodoDto.Todo t) throws ParseException {
        User user = modelMapper.map(t.getUser(), User.class);
        Todo todo = modelMapper.map(t, Todo.class);
        todo.setUser(user);

        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDto.Todo.class);
    }

    // 수정
    public TodoDto.Todo modify(TodoDto.Todo todo) throws Exception {
        User user = modelMapper.map(todo.getUser(), User.class);

        Todo savedTodo = todoRepository.findByTodoSeqAndUser(todo.getTodoSeq(), user)
                .orElseThrow(() -> new Exception("등록되지않은 할일 입니다."));
        savedTodo.setTitle(todo.getTitle());
        savedTodo.setCompleted(todo.getCompleted());

        Todo modifiedTodo = todoRepository.save(savedTodo);
        return modelMapper.map(modifiedTodo, TodoDto.Todo.class);
    }

    // 삭제
    @Transactional
    public void remove(Long todoSeq, Long userSeq) {
        User user = new User();
        user.setUserSeq(userSeq);

        todoRepository.deleteByTodoSeqAndUser(todoSeq, user);
    }

}
