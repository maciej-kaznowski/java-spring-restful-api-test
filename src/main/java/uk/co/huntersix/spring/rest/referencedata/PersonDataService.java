package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.model.Person;

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

    public Person findPerson(String lastName, String firstName) {
        List<Person> peopleForGivenName = PERSON_DATA.stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        if (peopleForGivenName.isEmpty()) return null;
        return peopleForGivenName.get(0);
    }
}
