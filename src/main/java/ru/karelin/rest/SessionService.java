package ru.karelin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.karelin.dto.Result;

import java.util.List;

@Service
public class SessionService {
    @Autowired
    CookieStorage cookieStorage;

    public boolean login(String login, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("login", login);
        map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        HttpEntity<Result> response = restTemplate.postForEntity("http://localhost:9090/taskman/rest/login", request, Result.class);
        if (response.getBody().isSuccess()) {
            List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
            cookieStorage.setCookies(cookies);
            return true;
        }
        return false;
    }

    public boolean logout() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.addAll("Cookie", cookieStorage.getCookies());
        ResponseEntity<Result> response = restTemplate.exchange(
                "http://localhost:9090/taskman/rest/logout",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                Result.class);
        return response.getBody()!=null && response.getBody().isSuccess();

    }
}
