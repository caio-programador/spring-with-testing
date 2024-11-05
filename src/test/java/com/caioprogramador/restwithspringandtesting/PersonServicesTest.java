package com.caioprogramador.restwithspringandtesting;

import com.caioprogramador.restwithspringandtesting.entities.Person;
import com.caioprogramador.restwithspringandtesting.exceptions.ResourceNotFoundException;
import com.caioprogramador.restwithspringandtesting.exceptions.UniqueFieldException;
import com.caioprogramador.restwithspringandtesting.repositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class})
class PersonServicesTest {

    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonServices services;

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
    @DisplayName("Given person object, when save person, then return person object")
    void testGivenPersonObject_WhenSavePerson_ShouldReturnPersonObject() {
        // Given
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person)).willReturn(person);
        // When

        Person savedPerson = services.create(person);
        // Then
        assertNotNull(savedPerson);
        assertEquals(person.getFirstName(), savedPerson.getFirstName());
    }

    @Test
    @DisplayName("Given existing email, when save person, should throws exception")
    void testGivenExistingEmail_WhenSavePerson_ShouldThrowsException() {
        // Given
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person));

        // When
        assertThrows(UniqueFieldException.class, () -> services.create(person));
        // Then
        verify(repository, never()).save(any(Person.class));
    }
    @Test
    @DisplayName("Given persons list, when find all persons, should return persons list")
    void testGivenPersonsList_WhenFindAllPersons_ShouldReturnPersonsList() {
        Person person2 = new Person(
                "Pedro",
                "Fonseca",
                "Venâncio Aires - RS",
                "Male",
                "pedro@gmail.com"
        );
        // Given
        given(repository.findAll()).willReturn(List.of(person, person2));
        // When

        List<Person> personList = services.findAll();
        // Then
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @Test
    @DisplayName("Given empty persons list, when find all persons, should return empty persons list")
    void testGivenEmptyPersonsList_WhenFindAllPersons_ShouldReturnEmptyPersonsList() {        // Given
        given(repository.findAll()).willReturn(Collections.emptyList());
        // When

        List<Person> personList = services.findAll();
        // Then
        assertTrue(personList.isEmpty());
    }

    @Test
    @DisplayName("Given person id, when find by id, should return person object")
    void testGivenPersonId_WhenFindById_ShouldReturnPersonObject() {
        // Given
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        // When

        Person personObj = services.findById(anyLong());
        // Then
        assertNotNull(personObj);
        assertEquals(person.getFirstName(), personObj.getFirstName());
    }
    @Test
    @DisplayName("Given non existed person id, when find by id, should throws exception")
    void testGivenNonExistedPersonId_WhenFindById_ShouldThrowsException() {
        // Given
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        // When
        assertThrows(ResourceNotFoundException.class, () -> services.findById(anyLong()));
        // Then
    }

    @Test
    @DisplayName("Given person object, when update person, should return person object")
    void testGivenPersonObject_WhenUpdatePerson_ShouldReturnPersonObject() {
        // Given
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        // When
        person.setEmail("lebinha@gmail.com");
        given(repository.save(person)).willReturn(person);

        Person personObj = services.update(person);
        // Then
        assertNotNull(personObj);
        assertNotEquals("caio@gmail.com", personObj.getEmail());
    }

    @Test
    @DisplayName("Given Person id, when delete person, should return nothing")
    void testGivenPersonId_WhenDeletePerson_ShouldReturnNothing() {
        // Given
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person));
        willDoNothing().given(repository).delete(person);
        // When
        services.delete(person.getId());
        // Then
        verify(repository, times(1)).delete(person);
    }

    @Test
    @DisplayName("Given non existed person id, when delete person, should throws exception")
    void testGivenNonExistedPersonId_WhenDeletePerson_ShouldThrowsException() {
        // Given
        person.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.empty());
        // When
        assertThrows(ResourceNotFoundException.class, () -> services.delete(person.getId()));
        // Then
        verify(repository, never()).delete(person);
    }


}