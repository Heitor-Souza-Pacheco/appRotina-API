package com.example.rotinaAPP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RotinaAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RotinaAppApplication.class, args);
    }
}
