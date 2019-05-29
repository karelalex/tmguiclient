package ru.karelin.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.karelin.dto.Result;
import ru.karelin.dto.TaskDto;

import java.util.List;

@FeignClient(url = "${app.restserver.url}", name = "taskRest", configuration = FeignConfig.class)
public interface TaskRestControllerI {
    @GetMapping(value = "/task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    TaskDto getTask(@PathVariable("id") String taskId);

    @GetMapping(value = "/task", produces = MediaType.APPLICATION_JSON_VALUE)
    List<TaskDto> getTaskList(@RequestParam(value = "projectId", required = false, defaultValue = "") String projectId);

    @PostMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result createTask (@RequestBody TaskDto task);

    @PutMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result editTask(@RequestBody TaskDto task);

    @DeleteMapping(value = "/task/{id}")
    Result removeTask(@PathVariable("id") String taskId);
}
