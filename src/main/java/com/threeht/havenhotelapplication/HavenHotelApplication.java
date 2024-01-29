package com.threeht.havenhotelapplication;

import com.threeht.havenhotelapplication.model.Room;
import com.threeht.havenhotelapplication.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@AllArgsConstructor
@SpringBootApplication
public class HavenHotelApplication {

    private final RoomRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(HavenHotelApplication.class, args);
    }

    @Bean
    CommandLineRunner populateDatabase() {
        return args -> {
            for (int i = 0; i < 10; i++) {
                var newRoom = Room.builder()
                        .roomPrice(BigDecimal.valueOf(200.0))
                        .roomType("Standard")
                        .isBooked(false)
                        .build();
                repository.save(newRoom);
            }

        };
    }

}
