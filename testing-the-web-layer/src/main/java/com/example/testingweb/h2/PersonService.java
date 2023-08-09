package com.example.testingweb.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person getPerson(Long id) {
        return personRepository.findById(id);
    }

    public Person addPerson(Person person) {
        personRepository.add(person);
        return person;
    }
}
