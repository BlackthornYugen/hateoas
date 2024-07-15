package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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
    void createAndVerifyPassport() {
        // Create Passport
        Passport passport = Passport.builder().country("Canada").expirationYear(2027).issueYear(2017).build();
        ResponseEntity<Passport> createResponse = restTemplate.postForEntity(baseUrl("/passports"), passport, Passport.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Passport createdPassport = createResponse.getBody();
        assertNotNull(createdPassport);
        assertNotNull(createdPassport.getId());

        // List Passports and verify creation
        var response = restTemplate.getForEntity(baseUrl("/passports"), CollectionModel.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getContent().stream().anyMatch(p -> p.toString().startsWith("{id=%d,".formatted(createdPassport.getId()))));

        // Get Passport by ID and verify
        ResponseEntity<Passport> getByIdResponse = restTemplate.getForEntity(baseUrl("/passports/" + createdPassport.getId()), Passport.class);
        assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
        assertEquals(createdPassport.getId(), Objects.requireNonNull(getByIdResponse.getBody()).getId());
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