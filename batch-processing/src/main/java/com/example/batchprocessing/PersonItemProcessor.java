package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * ItemProcessor< S, T > 를 통해, S 를 T로 변환하는 프로세스를 정의한다.
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) throws Exception {
        final String firstName = person.getFirst_name().toUpperCase();
        final String lastName = person.getLast_name().toUpperCase();
        final Person transformedPerson = new Person(person.getPerson_id(), firstName, lastName);

        log.info("Converting ({}) into ({})", person.toString(), transformedPerson.toString());
        return transformedPerson;
    }
}
