package ru.karelin.rest;


import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfig {

    @Bean
    public feign.okhttp.OkHttpClient client(JavaNetCookieJar jar) {
        final OkHttpClient build = new OkHttpClient.Builder()
                .cookieJar(jar)
                .build();
        return new  feign.okhttp.OkHttpClient(build);
    }
}
