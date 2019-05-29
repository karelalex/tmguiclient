package ru.karelin.rest;

import okhttp3.JavaNetCookieJar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.CookieManager;
import java.net.CookiePolicy;

@Configuration
public class BeanConfig {
    @Bean
    JavaNetCookieJar jar(){
        CookieManager cookieHandler = new CookieManager();
        cookieHandler.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        return new JavaNetCookieJar(cookieHandler);
    }
}
