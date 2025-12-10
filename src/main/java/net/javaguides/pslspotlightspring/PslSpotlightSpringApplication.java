package net.javaguides.pslspotlightspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PslSpotlightSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PslSpotlightSpringApplication.class, args);
    }

}
