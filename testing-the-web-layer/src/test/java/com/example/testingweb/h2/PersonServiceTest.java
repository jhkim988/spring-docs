package com.example.testingweb.h2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
public class PersonServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);

    @Autowired
    private PersonService service;

    @Test
    public void testGetPerson() {
        Assertions.assertNull(service.getPerson(1L));

        Person testObj = new Person("test", "name");
        Assertions.assertNull(testObj.getPerson_id());
        service.addPerson(testObj);

        Assertions.assertEquals(1L, testObj.getPerson_id());

        Person got = service.getPerson(1L);
        Assertions.assertNotNull(got);
        Assertions.assertEquals(1L, got.getPerson_id());
        Assertions.assertEquals(testObj.getFirst_name(), got.getFirst_name());
        Assertions.assertEquals( testObj.getLast_name(), got.getLast_name());
    }
}
