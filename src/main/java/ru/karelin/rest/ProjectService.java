package ru.karelin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.karelin.dto.ProjectDto;
import ru.karelin.dto.Result;

import java.util.Collections;
import java.util.List;

@Component
public class ProjectService {
    @Autowired
    CookieStorage cookieStorage;

    @Autowired
    ProjectMapStorage projectMapStorage;

    RestTemplate restTemplate = new RestTemplate();
    private final String URL = "http://localhost:9090/taskman/rest/project";

    public List<ProjectDto> getProjectList() {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        ResponseEntity<List<ProjectDto>> response = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                new ParameterizedTypeReference<List<ProjectDto>>() {
                });
        if (response.getBody() == null) {
            return Collections.EMPTY_LIST;
        }
        projectMapStorage.updateItems(response.getBody());
        return response.getBody();
    }

    public ProjectDto getProject(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        ResponseEntity<ProjectDto> response = restTemplate.exchange(
                URL + "/" + id,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                ProjectDto.class);
        if (response.getBody() == null) {
            return null;
        }
        projectMapStorage.addItem(response.getBody());
        return response.getBody();
    }

    public ProjectDto create() {
        ProjectDto projectInit = new ProjectDto();
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Result> response = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                new HttpEntity<>(projectInit, headers),
                Result.class);
        if (response.getBody().isSuccess()) {
            headers.setContentType(MediaType.TEXT_PLAIN);
            ResponseEntity<ProjectDto> response2 = restTemplate.exchange(
                    URL + "/" + projectInit.getId(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    ProjectDto.class);
            return response2.getBody();
        }
        return null;
    }

    public boolean update(ProjectDto projectDto) {
        projectMapStorage.removeItem(projectDto);
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Result> response = restTemplate.exchange(
                URL,
                HttpMethod.PUT,
                new HttpEntity<>(projectDto, headers),
                Result.class);
        return response.getBody() != null && response.getBody().isSuccess();
    }

    public boolean delete(ProjectDto projectDto) {
        projectMapStorage.removeItem(projectDto);
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Result> response = restTemplate.exchange(
                URL + "/" + projectDto.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Result.class);
        return response.getBody() != null && response.getBody().isSuccess();
    }
}
