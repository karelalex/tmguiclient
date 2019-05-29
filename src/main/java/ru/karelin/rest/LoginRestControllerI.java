package ru.karelin.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.karelin.dto.Result;

@FeignClient(url = "${app.restserver.url}" , name = "loginRest", configuration = FeignConfig.class)
public interface LoginRestControllerI {
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    Result login(@RequestParam(name = "login") String login,
                 @RequestParam(name = "password") String password);

    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    Result logout();
}
