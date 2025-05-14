package com.bilyoner.betting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.bilyoner.betting","com.bilyoner.betting.entity","com.bilyoner.betting.exceptions"})
@EnableJpaRepositories("com.bilyoner.betting.repository")
@EnableScheduling
public class BilyonerCaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BilyonerCaseApplication.class, args);
    }

}
