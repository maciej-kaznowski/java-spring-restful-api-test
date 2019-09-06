package uk.co.huntersix.spring.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void shouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/mary"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("firstName").value("Mary"))
            .andExpect(jsonPath("lastName").value("Smith"));
    }

    @Test
    public void shouldReturnNoContentWhenPersonNotFound() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(null);
        this.mockMvc.perform(get("/person/fullName/firstName"))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnPeopleFromService() throws Exception {
        when(personDataService.findPeople(any())).thenReturn(Arrays.asList(
            new Person("Mary", "Smith"),
            new Person("Adam", "Smith")
        ));
        this.mockMvc.perform(get("/person/smith"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].firstName").value("Mary"))
            .andExpect(jsonPath("$[0].lastName").value("Smith"))
            .andExpect(jsonPath("$[1].firstName").value("Adam"))
            .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    public void shouldReturnEmptyArrayForNoPeople() throws Exception {
        when(personDataService.findPeople(any())).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/person/smith"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldReturnArrayForSinglePerson() throws Exception {
        when(personDataService.findPeople(any())).thenReturn(Collections.singletonList(new Person("Mary", "Smith")));

        this.mockMvc.perform(get("/person/smith"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].firstName").value("Mary"))
            .andExpect(jsonPath("$[0].lastName").value("Smith"));
    }
}