package com.sch.crane.cranewebbackend_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CraneWebBackendV2Application {

    public static void main(String[] args) {
        SpringApplication.run(CraneWebBackendV2Application.class, args);
    }

}
