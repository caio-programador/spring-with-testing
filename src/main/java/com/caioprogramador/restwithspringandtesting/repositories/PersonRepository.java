package com.caioprogramador.restwithspringandtesting.repositories;

import com.caioprogramador.restwithspringandtesting.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
