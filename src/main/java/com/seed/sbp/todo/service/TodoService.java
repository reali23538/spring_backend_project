package com.seed.sbp.todo.service;

import com.seed.sbp.todo.domain.Todo;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    // 리스트(페이징)
    public TodoDto.TodoPage getTodoPage(TodoDto.TodoSearch search) {
        PageRequest pageRequest = PageRequest.of(search.getCurrentPage(), search.getLimit(), search.getSortType(), search.getOrderBy());
        Page<Todo> todoPage = todoRepository.findAllByCompleted(search.getCompleted(), pageRequest);

        List<TodoDto.Todo> todos = todoPage.map(todo -> TodoDto.Todo.builder()
                .todoSeq(todo.getTodoSeq())
                .title(todo.getTitle())
                .completed(todo.getCompleted())
                .regDate(todo.getStrRegDate())
                .build()
        ).getContent();

        return TodoDto.TodoPage.builder()
                .todos(todos)
                .totalRowCnt(todoPage.getTotalElements())
                .rowCntPerPage(search.getLimit())
                .build();
    }

    // 상세
    public TodoDto.Todo getTodo(Long todoSeq) throws Exception {
        return todoRepository.findById(todoSeq)
                .map(todo -> TodoDto.Todo.builder()
                        .todoSeq(todo.getTodoSeq())
                        .title(todo.getTitle())
                        .completed(todo.getCompleted())
                        .regDate(todo.getStrRegDate())
                        .build()
                ).orElseThrow(() -> new Exception("등록되지않은 할일 입니다."));
    }

    // 등록
    public TodoDto.Todo add(TodoDto.Todo t) throws ParseException {
        Todo todo = new Todo();
        todo.setTitle(t.getTitle());
        todo.setCompleted(t.getCompleted());
        todo.setRegDate(t.getRegDate());

        Todo savedTodo = todoRepository.save(todo);
        return TodoDto.Todo.builder()
                .todoSeq(savedTodo.getTodoSeq())
                .title(savedTodo.getTitle())
                .completed(savedTodo.getCompleted())
                .regDate(savedTodo.getStrRegDate())
                .build();
    }

    // 수정
    public TodoDto.Todo modify(TodoDto.Todo todo) throws Exception {
        Todo savedTodo = todoRepository.findById(todo.getTodoSeq())
                .orElseThrow(() -> new Exception("등록되지않은 할일 입니다."));
        savedTodo.setTitle(todo.getTitle());
        savedTodo.setCompleted(todo.getCompleted());

        Todo modifiedTodo = todoRepository.save(savedTodo);
        return TodoDto.Todo.builder()
                .todoSeq(modifiedTodo.getTodoSeq())
                .title(modifiedTodo.getTitle())
                .completed(modifiedTodo.getCompleted())
                .regDate(modifiedTodo.getStrRegDate())
                .build();
    }

    // 삭제
    public void remove(Long todoSeq) {
        todoRepository.deleteById(todoSeq);
    }

}
