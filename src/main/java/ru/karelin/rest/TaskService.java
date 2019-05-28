package ru.karelin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.karelin.dto.TaskDto;
import ru.karelin.dto.Result;
import ru.karelin.dto.TaskDto;

import java.util.Collections;
import java.util.List;

@Component
public class TaskService {
    @Autowired
    CookieStorage cookieStorage;

    private RestTemplate restTemplate = new RestTemplate();
    private final String URL = "http://localhost:9090/taskman/rest/task";

    public List<TaskDto> getTaskList(@Nullable final String projectId) {
        String url;
        if(projectId==null || projectId.isEmpty()){
            url=URL;
        }
        else {
            url=  UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("projectId", projectId)
                    .toUriString();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        ResponseEntity<List<TaskDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                new ParameterizedTypeReference<List<TaskDto>>() {
                });
        if (response.getBody() == null) {
            return Collections.EMPTY_LIST;
        }
        return response.getBody();
    }

    public TaskDto create(String projectId) {
        TaskDto taskInit = new TaskDto();
        taskInit.setProjectId(projectId);
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Result> response = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                new HttpEntity<>(taskInit, headers),
                Result.class);
        if (response.getBody().isSuccess()) {
            headers.setContentType(MediaType.TEXT_PLAIN);
            ResponseEntity<TaskDto> response2 = restTemplate.exchange(
                    URL + "/" + taskInit.getId(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    TaskDto.class);
            return response2.getBody();
        }
        return null;
    }

    public boolean update(TaskDto taskDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Result> response = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                new HttpEntity<>(taskDto, headers),
                Result.class);
        return response.getBody() != null && response.getBody().isSuccess();
    }

    public boolean delete(TaskDto taskDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Result> response = restTemplate.exchange(
                URL + "/" + taskDto.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Result.class);
        return response.getBody() != null && response.getBody().isSuccess();
    }
}
