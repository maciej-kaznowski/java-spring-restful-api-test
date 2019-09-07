package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static final List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    private final List<Person> people = new ArrayList<>(PERSON_DATA);

    public Person findPerson(String lastName, String firstName) {
        List<Person> peopleForGivenName = people.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
            .collect(Collectors.toList());

        if (peopleForGivenName.isEmpty()) {
            throw new PersonNotFoundException();
        }

        return peopleForGivenName.get(0);
    }

    /**
     * Finds all people with the given last name.
     *
     * @param lastName the last name of {@link Person} we are looking for, case-insensitive
     * @return a list of {@link Person} with the given lastName
     */
    public List<Person> findPeople(String lastName) {
        return people.stream()
            .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
            .collect(Collectors.toList());
    }

    public void addPerson(Person person) {
        if (hasPerson(person)) {
            throw new PersonAlreadyExistsException();
        }

        people.add(person);
    }

    private boolean hasPerson(Person person) {
        return hasPerson(person.getFirstName(), person.getLastName());
    }

    private boolean hasPerson(String firstName, String lastName) {
        return people.stream()
            .anyMatch(person -> person.getFirstName().equalsIgnoreCase(firstName) && person.getLastName().equalsIgnoreCase(lastName));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Person not found")
    public static class PersonNotFoundException extends RuntimeException {

        PersonNotFoundException() {
            super("Person not found");
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Person already exists")
    public static class PersonAlreadyExistsException extends RuntimeException {

        PersonAlreadyExistsException() {
            super("Person already exists");
        }
    }
}
