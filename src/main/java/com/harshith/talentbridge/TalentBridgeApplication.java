package com.harshith.talentbridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TalentBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalentBridgeApplication.class, args);
    }
}