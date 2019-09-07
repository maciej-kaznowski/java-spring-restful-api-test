package uk.co.huntersix.spring.rest.referencedata;

import org.junit.Test;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService.PersonAlreadyExistsException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PersonDataServiceTest {

    private PersonDataService service = new PersonDataService();

    @Test
    public void findPerson_shouldReturnPerson() {
        Person person = service.findPerson("Smith", "Mary");
        assertEquals("Mary", person.getFirstName());
        assertEquals("Smith", person.getLastName());
    }

    @Test
    public void findPerson_shouldIgnoreCapitalisation() {
        Person person = service.findPerson("smiTH", "mARY");
        assertEquals("Mary", person.getFirstName());
        assertEquals("Smith", person.getLastName());
    }

    @Test
    public void findPerson_shouldReturnNullForNoMatchingPeople() {
        Person person = service.findPerson("Snow", "Jon");
        assertNull(person);
    }

    @Test
    public void findPeople_shouldReturnAllPeople() {
        //service already contains `Mary Smith`, let's add John Smith
        Person marySmith = service.findPerson("Smith", "Mary");
        Person johnSmith = new Person("John", "Smith");
        service.addPerson(johnSmith);

        //should now return 2 people
        List<Person> actual = service.findPeople("Smith");
        List<Person> expected = Arrays.asList(marySmith, johnSmith);
        assertEquals(expected, actual);
    }

    @Test
    public void findPeople_shouldReturnEmptyListForNoMatches() {
        //shouldn't contain anyone with the last name "Stark"
        List<Person> starks = service.findPeople("Stark");
        assertTrue(starks.isEmpty());
    }

    @Test
    public void addPerson_shouldAdd() {
        //shouldn't contain Jon Snow yet
        assertNull(service.findPerson("Snow", "Jon"));

        Person jonSnow = new Person("Jon", "Snow");
        service.addPerson(jonSnow);

        //should contain Jon Snow
        Person foundPerson = service.findPerson("Snow", "Jon");
        assertEquals(jonSnow, foundPerson);
    }

    @Test(expected = PersonAlreadyExistsException.class)
    public void addPerson_shouldThrowWhenPersonExists() {
        Person anotherMary = new Person("Mary", "Smith");
        service.addPerson(anotherMary);
    }
}