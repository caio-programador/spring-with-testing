package com.caioprogramador.restwithspringandtesting.integrationtests.controllers;

import com.caioprogramador.restwithspringandtesting.config.TestConfig;
import com.caioprogramador.restwithspringandtesting.entities.Person;
import com.caioprogramador.restwithspringandtesting.integrationtests.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    static void setup(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/api/persons")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person(
                "Caio",
                "Fonseca",
                "Venâncio Aires - RS",
                "Male",
                "caio@gmail.com"
        );
    }

    @Test
    @Order(1)
    @DisplayName("Integration test given person object, when create one person, should return a person object")
    void integrationTestGivenPersonObject_WhenCreateOnePerson_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then().statusCode(200).extract().body().asString();
        Person createdPerson = objectMapper.readValue(content, Person.class);
        person = createdPerson;

        assertNotNull(createdPerson);
        assertTrue(createdPerson.getId() > 0);
        assertEquals("caio@gmail.com", createdPerson.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Integration test given person object, when update a Person, should return an updated person object")
    void integrationTestGivenPersonObject_WhenUpdateAPerson_ShouldReturnAnUpdatedPersonObject() throws JsonProcessingException {
        person.setEmail("caio12@gmail.com");
        var content = given()
                .spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then().statusCode(200).extract().body().asString();
        Person updatedPerson = objectMapper.readValue(content, Person.class);
        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertEquals("caio12@gmail.com", updatedPerson.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Integration test given person id, when find by id, should return a person object")
    void integrationTestGivenPersonId_WhenFindById_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .when()
                .get("/{id}", person.getId())
                .then().statusCode(200).extract().body().asString();
        Person getPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(getPerson);
        assertEquals("caio12@gmail.com", getPerson.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Integration test, when find all, should return a persons list")
    void integrationTest_WhenFindAll_ShouldReturnAPersonsList() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .when()
                .get()
                .then().statusCode(200).extract().body().asString();
        List<Person> persons = objectMapper.readValue(content, List.class);

        assertNotNull(persons);
        assertEquals(1, persons.size());
    }

    @Test
    @Order(5)
    @DisplayName("Integration test given person id, when delete, should return no content")
    void integrationTestGivenPersonId_WhenDelete_ShouldReturnNoContent() {
        given()
                .spec(specification)
                .when()
                .delete("/{id}", person.getId())
                .then().statusCode(204);
    }
}
