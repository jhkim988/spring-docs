package com.example.batchprocessing.batch.reader;

import com.example.batchprocessing.Person;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

public class PersonCSVReader {
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv")) // resource 를 읽는다.
                .delimited() // 구분자
                .names(new String[]{"firstName", "lastName"}) // 구분자로 구분하여 이름을 붙인다.
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{ // 이름에 맞게 Person 객체에 매핑한다.
                    setTargetType(Person.class);
                }})
                .build();
    }
}
