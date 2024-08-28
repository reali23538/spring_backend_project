package com.seed.sbp.todo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seed.sbp.common.response.CommonResult;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.service.TodoService;
import com.seed.sbp.todo.web.TodoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
//@ContextConfiguration
public class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

//    @Configuration
//    static class TestUserDetailsConfiguration {
//        @Bean("testUserDetailsService")
//        UserDetailsService testUserDetailsService() {
//            return new UserDetailsService() {
//                @Override
//                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                    User user = new User();
//                    user.setUserSeq(1L);
//                    return new SbpUserDetails(user);
//                }
//            };
//        }
//    }

    @DisplayName("할일 리스트 조회 테스트")
    @Test
//    @WithUserDetails(userDetailsServiceBeanName = "testUserDetailsService", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getTodoPageTest() throws Exception {
        // given
        doReturn(getTodoPage())
                .when(todoService)
                .getTodoPage(any(TodoDto.TodoSearch.class));

        // when
        // todo : userDetails 에서 userSeq 가져오는 부분. 현재는 주석 처리후 테스트 진행함
//        User user = new User();
//        user.setUserSeq(1L);
//        user.setEmail("sbpunit@sbp.com");
//        user.setPassword("1111");
//        UserDetails userDetails = new SbpUserDetails(user);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/todos")
                        .param("currentPage", "1")
                        .param("completed", "false")
//                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
        );

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        Type type = new TypeToken<CommonResult<TodoDto.TodoPage>>(){}.getType();
        CommonResult<TodoDto.TodoPage> commonResult = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), type);
        TodoDto.TodoPage todoPage = commonResult.getContents();
        System.out.println("todos size : " + todoPage.getTodos().size());

        assertThat(todoPage.getTodos().size()).isEqualTo(10);
        assertThat(todoPage.getTotalRowCnt()).isEqualTo(10);
        assertThat(todoPage.getRowCntPerPage()).isEqualTo(10);
    }

    @DisplayName("할일 상세 조회 테스트")
    @Test
    void getTodoTest() throws Exception {
        // given
        doReturn(getTodo(1))
                .when(todoService)
                .getTodo(anyLong(), anyLong());

        // when
        // todo : userDetails 에서 userSeq 가져오는 부분. 현재는 userSeq 값 대입 후 테스트 진행함
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/todos/1")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("contents.todoSeq", getTodo(1).getTodoSeq()).exists())
                .andExpect(jsonPath("contents.title", getTodo(1).getTitle()).exists())
                .andReturn();
        print(mvcResult);
    }

    @DisplayName("할일 등록 테스트")
    @Test
    void addTest() throws Exception {
        // given
        TodoDto.Todo todo = new TodoDto.Todo();
        todo.setTitle("할일1");
        todo.setCompleted(false);
        todo.setRegDate("202408261800");

        TodoDto.Todo savedTodo = getTodo(1);
        doReturn(savedTodo)
                .when(todoService)
                .add(any(TodoDto.Todo.class));

        // when
        // todo : userDetails 에서 userSeq 가져오는 부분. 현재는 주석 처리후 테스트 진행함
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(todo))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("contents.todoSeq", getTodo(1).getTodoSeq()).exists())
                .andExpect(jsonPath("contents.title", getTodo(1).getTitle()).exists())
                .andReturn();
        print(mvcResult);
    }

    @DisplayName("할일 수정 테스트")
    @Test
    void modifyTest() throws Exception {
        // given
        TodoDto.Todo todo = getTodo(1);
        doReturn(todo)
                .when(todoService)
                .modify(any(TodoDto.Todo.class));

        // when
        // todo : userDetails 에서 userSeq 가져오는 부분. 현재는 주석 처리후 테스트 진행함
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(todo))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("contents.todoSeq", getTodo(1).getTodoSeq()).exists())
                .andExpect(jsonPath("contents.title", getTodo(1).getTitle()).exists())
                .andReturn();
        print(mvcResult);
    }

    @DisplayName("할일 삭제 테스트")
    @Test
    void deleteTest() throws Exception {
        // given
        doNothing()
                .when(todoService)
                .remove(anyLong(), anyLong());

        // when
        // todo : userDetails 에서 userSeq 가져오는 부분. 현재는 userSeq 값 대입 후 테스트 진행함
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/todos/1")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andReturn();
        print(mvcResult);
    }

    private TodoDto.TodoPage getTodoPage() {
        List<TodoDto.Todo> todos = new ArrayList<>();
        for (long i=1; i<=10; i++) {
            TodoDto.Todo todo = getTodo(i);
            todos.add(todo);
        }

        TodoDto.TodoSearch search = new TodoDto.TodoSearch();
        return TodoDto.TodoPage.builder()
                .todos(todos)
                .totalRowCnt((long) todos.size())
                .rowCntPerPage(search.getLimit())
                .build();
    }
    private TodoDto.Todo getTodo(long i) {
        TodoDto.Todo todo = new TodoDto.Todo();
        todo.setTodoSeq(i);
        todo.setTitle("할일" + i);
        todo.setCompleted(false);
        todo.setRegDate("202408261800");
        todo.setUser(new TodoDto.User(1L));
        return todo;
    }

    private void print(MvcResult mvcResult) throws UnsupportedEncodingException {
        System.out.println("response : " + mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
    }
}
