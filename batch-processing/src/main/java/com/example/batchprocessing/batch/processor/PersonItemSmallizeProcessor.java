package com.example.batchprocessing.batch.processor;

import com.example.batchprocessing.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonItemSmallizeProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person item) throws Exception {
        Person transported = new Person();
        transported.setPerson_id(item.getPerson_id());
        transported.setFirst_name(item.getFirst_name().toLowerCase());
        transported.setLast_name(item.getLast_name().toLowerCase());
        return transported;
    }
}
