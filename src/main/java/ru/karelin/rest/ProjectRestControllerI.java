package ru.karelin.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.karelin.dto.ProjectDto;
import ru.karelin.dto.Result;

import java.util.List;

@FeignClient(url = "${app.restserver.url}", name = "projectRest", configuration = FeignConfig.class)
public interface ProjectRestControllerI {
    @GetMapping(value = "/project", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProjectDto> getProjectList();

    @GetMapping(value = "/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProjectDto getProject(@PathVariable("id") String projectId);

    @PostMapping(value = "/project", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result createProject(@RequestBody ProjectDto project);

    @PutMapping(value = "/project", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result editProject(@RequestBody ProjectDto project);

    @DeleteMapping(value = "project/{id}")
    Result removeProject(@PathVariable("id") String projectId);
}
