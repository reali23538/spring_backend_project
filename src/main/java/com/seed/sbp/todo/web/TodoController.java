package com.seed.sbp.todo.web;

import com.seed.sbp.common.exception.NoContentException;
import com.seed.sbp.common.response.CommonResult;
import com.seed.sbp.common.response.CommonResultCode;
import com.seed.sbp.common.response.ResponseProvider;
import com.seed.sbp.todo.domain.TodoDto;
import com.seed.sbp.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Tag(name = "Todo API", description = "Todo API 입니다.")
@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // 리스트(페이징)
    @Operation(summary = "할일 리스트", description = "페이징된 할일 리스트를 조회하는 API 입니다.")
    @Parameters(value = {
            @Parameter(name = "currentPage", description = "현재 페이지"),
            @Parameter(name = "completed", description = "완료여부")
    })
    @ApiResponse(responseCode = "200", description = "리스트 조회 성공", content = @Content(schema = @Schema(implementation = TodoDto.TodoPage.class)))
    @GetMapping("/todos")
    public ResponseEntity<CommonResult<TodoDto.TodoPage>> getTodoPage(
        @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
        @RequestParam(value = "completed", required = false, defaultValue = "false") Boolean completed
    ) {
        TodoDto.TodoSearch search = new TodoDto.TodoSearch();
        search.setCurrentPage(currentPage);
        search.setOrderBy("todoSeq");
        search.setCompleted(completed);

        return ResponseProvider.ok(todoService.getTodoPage(search));
    }

    // 상세
    @Operation(summary = "할일 상세", description = "할일 상세내역을 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상세 조회 성공", content = @Content(schema = @Schema(implementation = TodoDto.Todo.class))),
            @ApiResponse(responseCode = "204", description = "내역 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/todos/{todoSeq}")
    public ResponseEntity<CommonResult<TodoDto.Todo>> getTodo(
            @Parameter(name = "todoSeq", description = "할일 seq", in = ParameterIn.PATH)
            @PathVariable(name = "todoSeq") Long todoSeq
    ) {
        try {
            return ResponseProvider.ok(todoService.getTodo(todoSeq));
        } catch (NoContentException nce) {
            return ResponseProvider.fail(CommonResultCode.COMMON_NO_CONTENT);
        }
    }

    // 등록
    @Operation(summary = "할일 등록", description = "할일을 등록하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = TodoDto.Todo.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인한 실패", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/todos")
    public ResponseEntity<CommonResult<TodoDto.Todo>> add(@RequestBody @Valid TodoDto.Todo todo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseProvider.getResponseEntity(CommonResultCode.COMMON_INVALID_PARAMS, bindingResult.getAllErrors());
        }

        try {
            return ResponseProvider.getResponseEntity(todoService.add(todo), CommonResultCode.SUCCESS_CREATE);
        } catch (ParseException e) {
            return ResponseProvider.fail(CommonResultCode.COMMON_INVALID_PARAMS);
        }
    }

    // 수정
    @Operation(summary = "할일 수정", description = "할일을 수정하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = TodoDto.Todo.class))),
            @ApiResponse(responseCode = "204", description = "컨텐츠 없음", content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/todos/{todoSeq}")
    public ResponseEntity<CommonResult<TodoDto.Todo>> modify(
            @Parameter(name = "todoSeq", description = "할일 seq", in = ParameterIn.PATH)
            @PathVariable(name = "todoSeq") Long todoSeq,
            @RequestBody TodoDto.Todo todo
    ) {
        try {
            return ResponseProvider.ok(todoService.modify(todo));
        } catch (Exception e) {
            return ResponseProvider.fail(CommonResultCode.COMMON_NO_CONTENT);
        }
    }

    // 삭제
    @Operation(summary = "할일 삭제", description = "할일을 삭제하는 API 입니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(hidden = true)))
    @DeleteMapping("/todos/{todoSeq}")
    public ResponseEntity<CommonResult<Object>> remove(
            @Parameter(name = "todoSeq", description = "할일 seq", in = ParameterIn.PATH)
            @PathVariable(name = "todoSeq") Long todoSeq
    ) {
        todoService.remove(todoSeq);
        return ResponseProvider.ok();
    }

}
