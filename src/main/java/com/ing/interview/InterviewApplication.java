package com.ing.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ing.interview"})
public class InterviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterviewApplication.class, args);
    }
}
