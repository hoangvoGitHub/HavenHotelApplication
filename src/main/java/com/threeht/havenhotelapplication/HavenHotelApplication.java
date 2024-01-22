package com.threeht.havenhotelapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class HavenHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(HavenHotelApplication.class, args);
    }

}
