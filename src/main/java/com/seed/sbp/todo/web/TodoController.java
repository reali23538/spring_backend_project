package com.seed.sbp.todo.web;

import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // 리스트(페이징)
    @GetMapping("/todos")
    public ResponseEntity<TodoDto.TodoPage> getTodoPage(
        @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
        @RequestParam(value = "completed", required = false, defaultValue = "false") Boolean completed
    ) {
        TodoDto.TodoSearch search = new TodoDto.TodoSearch();
        search.setCurrentPage(currentPage);
        search.setOrderBy("todoSeq");
        search.setCompleted(completed);

        return ResponseEntity.ok(todoService.getTodoPage(search));
    }

    // 상세
    @GetMapping("/todos/{todoSeq}")
    public ResponseEntity<TodoDto.Todo> getTodo(@PathVariable(name = "todoSeq") Long todoSeq) {
        try {
            return ResponseEntity.ok(todoService.getTodo(todoSeq));
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    // 등록
    @PostMapping("/todos")
    public ResponseEntity<TodoDto.Todo> add(@RequestBody TodoDto.Todo todo) {
        try {
            return new ResponseEntity(todoService.add(todo), HttpStatus.CREATED);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 수정
    @PutMapping("/todos/{todoSeq}")
    public ResponseEntity<TodoDto.Todo> modify(@RequestBody TodoDto.Todo todo) {
        try {
            return ResponseEntity.ok(todoService.modify(todo));
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    // 삭제
    @DeleteMapping("/todos/{todoSeq}")
    public ResponseEntity<?> remove(@PathVariable(name = "todoSeq") Long todoSeq) {
        todoService.remove(todoSeq);
        return ResponseEntity.ok().build();
    }

}
