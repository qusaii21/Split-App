package com.DevDynamics.splitapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SplitAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SplitAppBackendApplication.class, args);
    }
}