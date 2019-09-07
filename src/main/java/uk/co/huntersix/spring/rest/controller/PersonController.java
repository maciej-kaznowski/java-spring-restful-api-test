package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.List;

@RestController
public class PersonController {
    private PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<Person> person(@PathVariable(value = "lastName") String lastName,
                                         @PathVariable(value = "firstName") String firstName) {
        Person person = personDataService.findPerson(lastName, firstName);

        if (person == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(person, HttpStatus.OK);
        }
    }

    @GetMapping("/person/{lastName}")
    public List<Person> people(@PathVariable(value = "lastName") String lastName) {
        return personDataService.findPeople(lastName);
    }

    @PostMapping("/person")
    public Person create(@RequestBody Person person) {
        personDataService.addPerson(person);
        return person;
    }
}