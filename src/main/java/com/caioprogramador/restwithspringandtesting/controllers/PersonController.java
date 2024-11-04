package com.caioprogramador.restwithspringandtesting.controllers;

import java.util.List;

import com.caioprogramador.restwithspringandtesting.PersonServices;
import com.caioprogramador.restwithspringandtesting.entities.Person;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/persons")
public class PersonController {

	private PersonServices service;

    public PersonController(PersonServices service) {
        this.service = service;
    }

	@RequestMapping(method=RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Person> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Person findById(@PathVariable Long id) {
		return service.findById(id);
	}

	@PostMapping
	public Person create(@RequestBody Person person) {
		return service.create(person);
	}

	@PutMapping
	public Person update(@RequestBody Person person) {
		return service.update(person);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
        return ResponseEntity.noContent().build();
	}
}