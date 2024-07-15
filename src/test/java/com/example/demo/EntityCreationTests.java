package com.example.demo;

import com.example.demo.rest.entities.Animal;
import com.example.demo.rest.entities.Dog;
import com.example.demo.rest.entities.Human;
import com.example.demo.rest.entities.Passport;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        var response = restTemplate.exchange(
                baseUrl("/passports"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CollectionModel<Passport>>() {}
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(listContainsId(response, createdPassport.getId())).isTrue();

        // Get Passport by ID and verify
        ResponseEntity<Passport> getByIdResponse = restTemplate.getForEntity(baseUrl("/passports/" + createdPassport.getId()), Passport.class);
        assertEquals(HttpStatus.OK, getByIdResponse.getStatusCode());
        assertEquals(createdPassport.getId(), getId(getByIdResponse));
    }

    @Test
    void createDog() {
        Dog dog = Dog.builder().name("Buddy").breed("Labrador").isChipped(true).dna(new byte[]{3,4,5}).build();
        ResponseEntity<Dog> response = restTemplate.postForEntity(baseUrl("/animals"), dog, Dog.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Dog createdDog = response.getBody();
        assertNotNull(createdDog);
        assertNotNull(createdDog.getId());
        assertArrayEquals(dog.getDna(), createdDog.getDna());
        assertEquals(dog.getName(), createdDog.getName());
    }

    @Test
    void createHuman() {
        var passport = Passport.builder().country("Canada").expirationYear(2030).issueYear(2020).build();
        var createPassportResponse = restTemplate.postForEntity(baseUrl("/passports"), passport, Passport.class);
        var human = Human.builder().name("Jane Doe").dna(new byte[]{1,2,3}).passports(List.of(passport)).build();
        var createHumanResponse = restTemplate.postForEntity(baseUrl("/animals"), human, Human.class);

        var listPassportResponse = restTemplate.exchange(
                baseUrl("/passports"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CollectionModel<Passport>>() {}
        );
        var listHumanResponse = restTemplate.exchange(
                baseUrl("/animals"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CollectionModel<Animal>>() {}
        );
        var getByIdResponse = restTemplate.getForEntity(baseUrl("/animals/" + getId(createHumanResponse)), Human.class);
        var getPassportByIdResponse = restTemplate.exchange(
                baseUrl("/passports/" + getId(createPassportResponse)),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Passport>() {}
        );
        var getPassportByHumanResponse = restTemplate.exchange(
                baseUrl("/animals/" + getId(createHumanResponse) + "/passports"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CollectionModel<Passport>>() {}
        );
        var getHumanByPassportResponse = restTemplate.exchange(
                baseUrl("/passports/" + getId(createPassportResponse) + "/owner"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Human>() {}
        );

        SoftAssertions.assertSoftly(assertions -> {
            assertions.assertThat(createPassportResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertions.assertThat(createHumanResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

            assertions.assertThat(listPassportResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertions.assertThat(listContainsId(listPassportResponse, getId(createPassportResponse))).isTrue();

            assertions.assertThat(listHumanResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertions.assertThat(listContainsId(listHumanResponse, getId(createHumanResponse))).isTrue();

            assertions.assertThat(getByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertions.assertThat(getId(getByIdResponse)).isEqualTo(getId(createHumanResponse));

            assertions.assertThat(getPassportByIdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertions.assertThat(getId(getPassportByIdResponse)).isEqualTo(getId(createPassportResponse));

            assertions.assertThat(getPassportByHumanResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertions.assertThat(listContainsId(getPassportByHumanResponse, getId(createPassportResponse))).isTrue();

            assertions.assertThat(getHumanByPassportResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertions.assertThat(getId(getHumanByPassportResponse)).isEqualTo(getId(createHumanResponse));
        });
    }

    private static long getId(ResponseEntity<?> getHumanByPassportResponse) {
        if (getHumanByPassportResponse.getBody() instanceof Human)
            return ((Human) getHumanByPassportResponse.getBody()).getId();

        if (getHumanByPassportResponse.getBody() instanceof Passport)
            return ((Passport) getHumanByPassportResponse.getBody()).getId();

        return -1;
    }

    public static <T> boolean listContainsId(ResponseEntity<CollectionModel<T>> response, Long id) {
        if (response.getBody() == null) {
            return false;
        }
        return response.getBody().getContent().stream()
                .anyMatch(entity -> entity.toString().contains("id=" + id + ","));
    }
}