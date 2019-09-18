package com.pancarte.architecte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootConfiguration
public class ArchitecteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArchitecteApplication.class, args);
    }
}
