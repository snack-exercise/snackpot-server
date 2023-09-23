package com.soma;

import org.springframework.boot.SpringApplication;

//@SpringBootApplication // 테스트 실행 시, 에러 발생에 따라 주석 처리 [Error] Found multiple @SpringBootConfiguration annotated classes
//@EnableJpaAuditing
public class SnackpotCommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnackpotCommonApplication.class, args);
    }
}