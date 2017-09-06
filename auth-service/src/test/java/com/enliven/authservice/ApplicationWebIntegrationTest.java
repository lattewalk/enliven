package com.enliven.authservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jshi on 9/1/17.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationWebIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test() {
        restTemplate.getForEntity(
            "/{username}/vehicle", String.class, "Phil");
    }

}
