package com.caioprogramador.restwithspringandtesting.repositories;

import com.caioprogramador.restwithspringandtesting.entities.Person;
import com.caioprogramador.restwithspringandtesting.integrationtests.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private PersonRepository personRepository;
    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("Caio", "Fonseca",
                "Uberlândia - MG", "Male", "caiofonseca@gmail.com");
    }

    @Test
    @DisplayName("Given Person Object When Save Should Return Saved Person")
    void testGivenPersonObject_WhenSave_ShouldReturnSavedPerson() {
        // When
        Person savedPerson = personRepository.save(person);
        // Then
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }


    @Test
    @DisplayName("Given Person List When Find All Should Return Person List")
    void testGivenPersonList_WhenFindAll_ShouldReturnPersonList() {
        // Given
        Person person1 = new Person("Caio", "Fonseca",
                "Uberlândia - MG", "Male", "caiofonseca@gmail.com");
        Person person2 = new Person("Pedro", "Fonseca",
                "Uberlândia - MG", "Male", "pedro@gmail.com");
        personRepository.save(person1);
        personRepository.save(person2);
        // When
        List<Person> persons = personRepository.findAll();
        // Then
        assertNotNull(persons);
        assertEquals(2, persons.size());
    }

    @Test
    @DisplayName("Given Person Object When Find By Id Should Return Person")
    void testGivenPersonObject_WhenFindById_ShouldReturnPersonObject() {
        // Given
        personRepository.save(person);
        // When
        Person getPerson = personRepository.findById(person.getId()).get();
        // Then
        assertNotNull(getPerson);
        assertEquals(getPerson.getId(), person.getId());
    }

    @Test
    @DisplayName("Given Person Object When Find By Email Should Return Person")
    void testGivenPersonObject_WhenFindByEmail_ShouldReturnPersonObject() {
        // Given
        personRepository.save(person);
        // When
        Person getPerson = personRepository.findByEmail(person.getEmail()).get();
        // Then
        assertNotNull(getPerson);
        assertEquals(getPerson.getId(), person.getId());
    }

    @Test
    @DisplayName("Given Person Object when Update Person Should Return Updated Person Object")
    void testGivenPersonObject_WhenUpdatePerson_ShouldReturnUpdatedPersonObject() {
        // Given
        personRepository.save(person);
        // When
        Person personUpdated = personRepository.findById(person.getId()).get();
        personUpdated.setFirstName("Lucas");
        personUpdated.setLastName("Rodrigo");

        personUpdated = personRepository.save(personUpdated);
        // Then

        assertNotNull(personUpdated);
        assertEquals("Lucas", personUpdated.getFirstName());
    }

    @Test
    @DisplayName("Given Person Object when Update Person Should Return Updated Person Object")
    void testGivenPersonObject_WhenDelete_ShouldRemovePerson() {
        // Given
        personRepository.save(person);
        // When
        Person person1 = personRepository.findById(person.getId()).get();
        personRepository.delete(person1);

        Optional<Person> person2 = personRepository.findById(person.getId());
        // Then
        assertTrue(person2.isEmpty());
    }

    @Test
    @DisplayName("Given Person Object When Find By JPQL Should Return Person")
    void testGivenFirstNameAndLastName_WhenFindByJPQL_ShouldReturnPerson() {
        // Given
        personRepository.save(person);
        // When
        Person getPerson = personRepository.findByJPQL("Caio", "Fonseca");
        // Then
        assertNotNull(getPerson);
        assertEquals(getPerson.getId(), person.getId());
    }
}