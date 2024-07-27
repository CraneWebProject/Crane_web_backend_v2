package com.sch.crane.cranewebbackend_v2.Infrastructure.Config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("x-requested-with", "X-Auth-Token", "Content-Type", "x-access-token", "Authorization")
                .allowCredentials(true)
                .maxAge(60*60*24);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer(){
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("https://localhost:5173")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("x-requested-with", "X-Auth-Token", "Content-Type", "x-access-token", "Authorization")
//                        .allowCredentials(true)
//                        .maxAge(3600);
//            }
//        };
//    }
}
