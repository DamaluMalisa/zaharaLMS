package com.zahara.lms.exam;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients({"com.zahara.lms.exam", "com.zahara.lms.shared"})
@ComponentScan({"com.zahara.lms.exam", "com.zahara.lms.shared"})
@OpenAPIDefinition(
        info =
                @Info(
                        title = "Exam API",
                        version = "1.0",
                        description = "Documentation Exam API v1.0"))
public class ExamServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamServiceApplication.class, args);
    }
}
