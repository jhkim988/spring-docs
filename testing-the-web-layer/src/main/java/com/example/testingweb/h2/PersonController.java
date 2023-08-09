package com.example.testingweb.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;
    @GetMapping("/person/{id}")
    public Person getPerson(@PathVariable Long id) {
        return personService.getPerson(id);
    }
}
