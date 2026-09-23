package com.noesis.triage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NoesisApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoesisApplication.class, args);
    }
}
