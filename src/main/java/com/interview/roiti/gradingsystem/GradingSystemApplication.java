package com.interview.roiti.gradingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GradingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradingSystemApplication.class, args);
    }

}
