package com.enliven.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class to launch auth-service
 *
 * @author lattewalk
 */
@SpringBootApplication
public class Application {

    /**
     * The main method
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
