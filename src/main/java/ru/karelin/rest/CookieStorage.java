package ru.karelin.rest;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CookieStorage {
private List<String> list = Collections.EMPTY_LIST;

    public List<String> getCookies() {
        return list;
    }

    public void setCookies(List<String> list) {
        if(list==null){
            this.list = Collections.emptyList();
        }
        else this.list = list;

    }
}
