package com.sparta.catubebatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CatubeBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatubeBatchApplication.class, args);
    }

}
