package com.seed.sbp.common.communication.typicode.service;

import com.seed.sbp.common.communication.typicode.domain.TypicodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class TypicodeApiService {

    private final RestTemplate restTemplate;

    @Value("${external-api.typicode.url}")
    private String TYPICODE_URL;

    @Value("${external-api.typicode.todo-path}")
    private String TODO_PATH;

    public TypicodeApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TypicodeDto.Todo> get() {
        String url = TYPICODE_URL + TODO_PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<TypicodeDto.Todo>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("responseEntity : {}" + responseEntity);
        List<TypicodeDto.Todo> todos = responseEntity.getBody();
        return todos;
    }

    public TypicodeDto.Todo post(TypicodeDto.AddTodo todo) {
        String url = TYPICODE_URL + TODO_PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(todo, headers);

        ResponseEntity<TypicodeDto.Todo> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("responseEntity : {}" + responseEntity);
        return responseEntity.getBody();
    }

    public TypicodeDto.Todo put(TypicodeDto.Todo todo) {
        String url = TYPICODE_URL + TODO_PATH + "/" + todo.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(todo, headers);

        ResponseEntity<TypicodeDto.Todo> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {});
        log.info("responseEntity : {}" + responseEntity);
        return responseEntity.getBody();
    }

    public String delete(Long id) {
        String url = TYPICODE_URL + TODO_PATH + "/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
        log.info("responseEntity : {}" + responseEntity);
        return responseEntity.getBody();
    }

}
