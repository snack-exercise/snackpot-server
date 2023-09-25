package com.soma;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@OpenAPIDefinition(servers = {@Server(url = "/", description = "https://dev-api.snackexercise.com")})
@SpringBootApplication
@EnableJpaAuditing
public class SnackpotApiApplication {
    @PostConstruct
    public void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        log.info("현재시각 : " + new Date());
    }
    public static void main(String[] args) {
        SpringApplication.run(SnackpotApiApplication.class, args);
    }
}