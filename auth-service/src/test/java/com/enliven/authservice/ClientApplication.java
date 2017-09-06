package com.enliven.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Properties;

/**
 * Client application for the authservice
 *
 * @author lattewalk
 */

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class ClientApplication {
    @RequestMapping("/")
    public String home(Principal user) {
        return "Hello"  + user.getName();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ClientApplication.class);
        Properties props = new Properties();
        props.put("spring.config.name", "client");
        app.setDefaultProperties(props);
        app.run(args);
    }
}
