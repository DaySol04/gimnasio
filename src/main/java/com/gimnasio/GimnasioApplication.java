package com.gimnasio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.gimnasio.repository")
public class GimnasioApplication {

    public static void main(String[] args) {
        SpringApplication.run(GimnasioApplication.class, args);
    }

}
