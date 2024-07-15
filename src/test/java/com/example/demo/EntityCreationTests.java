package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EntityCreationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void createPassport() {
        Passport passport = Passport.builder().country("Canada").expirationYear(2027).issueYear(2017).build();
        ResponseEntity<Passport> response = restTemplate.postForEntity(baseUrl("/passports"), passport, Passport.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Additional assertions to verify the Passport entity
    }

    @Test
    void createDog() {
        Dog dog = new Dog(null, "Buddy", new byte[]{}, "Labrador", true);
        ResponseEntity<Dog> response = restTemplate.postForEntity(baseUrl("/animals"), dog, Dog.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Additional assertions to verify the Dog entity
    }

    @Test
    void createHuman() {
        Human human = new Human(null, "John Doe", new byte[]{}, null);
        ResponseEntity<Human> response = restTemplate.postForEntity(baseUrl("/animals"), human, Human.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        // Additional assertions to verify the Human entity
    }
}