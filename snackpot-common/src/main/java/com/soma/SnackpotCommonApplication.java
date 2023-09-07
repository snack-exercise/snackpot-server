package com.soma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class SnackpotCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnackpotCommonApplication.class, args);
    }
}