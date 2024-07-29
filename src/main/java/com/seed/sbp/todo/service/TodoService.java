package com.seed.sbp.todo.service;

import com.seed.sbp.common.exception.SbpException;
import com.seed.sbp.common.response.CommonResultCode;
import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.repository.TodoRepository;
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
        PageRequest pageRequest = PageRequest.of(search.getCurrentPage(), search.getLimit(), search.getSortType(), search.getOrderBy());
        Page<Todo> todoPage = todoRepository.findAllByCompleted(search.getCompleted(), pageRequest);

        List<TodoDto.Todo> todos = todoPage.map(todo ->
                modelMapper.map(todo, TodoDto.Todo.class)).getContent();

        return TodoDto.TodoPage.builder()
                .todos(todos)
                .totalRowCnt(todoPage.getTotalElements())
                .rowCntPerPage(search.getLimit())
                .build();
    }

    // 상세
    public TodoDto.Todo getTodo(Long todoSeq) throws SbpException {
        return todoRepository.findById(todoSeq)
                .map(todo -> modelMapper.map(todo, TodoDto.Todo.class))
                .orElseThrow(() -> new SbpException(CommonResultCode.COMMON_NO_CONTENT));
    }

    // 등록
    public TodoDto.Todo add(TodoDto.Todo t) throws ParseException {
        Todo todo = new Todo();
        todo.setTitle(t.getTitle());
        todo.setCompleted(t.getCompleted());
        todo.setRegDate(t.getRegDate());

        Todo savedTodo = todoRepository.save(todo);
        return modelMapper.map(savedTodo, TodoDto.Todo.class);
    }

    // 수정
    public TodoDto.Todo modify(TodoDto.Todo todo) throws Exception {
        Todo savedTodo = todoRepository.findById(todo.getTodoSeq())
                .orElseThrow(() -> new Exception("등록되지않은 할일 입니다."));
        savedTodo.setTitle(todo.getTitle());
        savedTodo.setCompleted(todo.getCompleted());

        Todo modifiedTodo = todoRepository.save(savedTodo);
        return modelMapper.map(modifiedTodo, TodoDto.Todo.class);
    }

    // 삭제
    public void remove(Long todoSeq) {
        todoRepository.deleteById(todoSeq);
    }

}
