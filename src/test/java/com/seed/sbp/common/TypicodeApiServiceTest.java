package com.seed.sbp.common;

import com.seed.sbp.common.communication.typicode.domain.TypicodeDto;
import com.seed.sbp.common.communication.typicode.service.TypicodeApiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
public class TypicodeApiServiceTest {

    @Autowired
    private TypicodeApiService typicodeApiService;

    @DisplayName("restTemplate get 테스트")
    @Test
    void getTest() {
        // given

        // when
        List<TypicodeDto.Todo> todos = typicodeApiService.get();
        System.out.println("todos size >>> " + todos.size());

        // then
        assertThat(todos.size()).isEqualTo(200);
    }

    @DisplayName("restTemplate post 테스트")
    @Test
    void postTest() {
        // given
        TypicodeDto.AddTodo todo = new TypicodeDto.AddTodo();
        todo.setUserId(1L);
        todo.setTitle("clean room");
        todo.setCompleted(false);

        // when
        TypicodeDto.Todo savedTodo = typicodeApiService.post(todo);
        System.out.println("savedTodo >>> " + savedTodo);

        // then
        assertThat(savedTodo.getId()).isEqualTo(201);
    }

    @DisplayName("restTemplate put 테스트")
    @Test
    void putTest() {
        // given
        TypicodeDto.Todo todo = new TypicodeDto.Todo();
        todo.setId(1L);
        todo.setUserId(1L);
        todo.setTitle("delectus aut autem1");
        todo.setCompleted(false);

        // when
        TypicodeDto.Todo modifiedTodo = typicodeApiService.put(todo);
        System.out.println("modifiedTodo >>> " + modifiedTodo);

        // then
        assertThat(modifiedTodo.getTitle()).isEqualTo("delectus aut autem1");
    }

    @DisplayName("restTemplate delete 테스트")
    @Test
    void deleteTest() {
        // given
        Long id = 1L;

        // when
        String result = typicodeApiService.delete(id);
        System.out.println("result >>> " + result);

        // then
        assertThat(result).isEqualTo("{}");
    }

}
