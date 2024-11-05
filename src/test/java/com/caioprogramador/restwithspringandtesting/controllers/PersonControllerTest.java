package com.caioprogramador.restwithspringandtesting.controllers;

import com.caioprogramador.restwithspringandtesting.PersonServices;
import com.caioprogramador.restwithspringandtesting.entities.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonServices service;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person(
                "Caio",
                "Fonseca",
                "Venâncio Aires - RS",
                "Male",
                "caio@gmail.com"
        );
    }

    @Test
    @DisplayName("Given person object, when create person, should return saved person")
    void testGivenPersonObject_WhenCreatePerson_ShouldReturnSavedPerson() throws Exception {
        // Given
        given(service.create(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        // When
        ResultActions response = mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)));
        // Then
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }


    @Test
    @DisplayName("Given person list, when find all persons, should return person list")
    void testGivenPersonList_WhenFindAllPersons_ShouldReturnPersonList() throws Exception {
        // Given
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(new Person(
                "João",
                "Silva",
                "Porto Alegre - RS",
                "Male",
                "jao@gmail.com"
        ));
        given(service.findAll()).willReturn(persons);
        // When
        ResultActions response = mockMvc.perform(get("/api/persons"));
        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())))
                .andExpect(jsonPath("$[0].firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$[0].email", is(person.getEmail())));
    }


    @Test
    @DisplayName("Given person id, when find by id, should return person object")
    void testGivenPersonId_WhenFindById_ShouldReturnPersonObject() throws Exception {
        // Given
        long personId = 1L;
        given(service.findById(personId)).willReturn(person);
        // When
        ResultActions response = mockMvc.perform(get("/api/persons/{id}", personId));
        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("Given person object, when update person, should return person object")
    void testGivenUpdatePerson_WhenUpdate_ShouldReturnPersonObject() throws Exception {
        // Given
        person.setId(1L);
        person.setEmail("ney@gmail.com");
        given(service.findById(person.getId())).willReturn(person);
        given(service.update(any(Person.class))).willAnswer(invocation -> invocation.getArgument(0));
        // When

        ResultActions response = mockMvc.perform(put("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
        );
        // Then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }


    @Test
    @DisplayName("Given person id, when delete person, should return nothing")
    void testGivenPersonId_WhenDelete_ShouldReturnNothing() throws Exception {
        // Given
        long personId = 1L;
        willDoNothing().given(service).delete(personId);
        // When

        ResultActions response = mockMvc.perform(delete("/api/persons/{id}", personId));
        // Then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }

}