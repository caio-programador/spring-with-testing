package com.caioprogramador.restwithspringandtesting;

import com.caioprogramador.restwithspringandtesting.entities.Person;
import com.caioprogramador.restwithspringandtesting.exceptions.ResourceNotFoundException;
import com.caioprogramador.restwithspringandtesting.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServices {
    private final PersonRepository personRepository;

    public PersonServices(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findById(Long id){
        return personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No person found for this ID"));
    }

    public Person create(Person person){
        return personRepository.save(person);
    }

    public Person update(Person person){
        Person entity = personRepository.findById(person.getId()).orElseThrow(
                () -> new ResourceNotFoundException("No person found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return personRepository.save(entity);
    }

    public void delete(Long id){
        Person entity = personRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No person found for this ID"));
        personRepository.delete(entity);
    }


}
