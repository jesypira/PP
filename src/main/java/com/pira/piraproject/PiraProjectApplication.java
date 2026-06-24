package com.pira.piraproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class PiraProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiraProjectApplication.class, args);
    }

}
