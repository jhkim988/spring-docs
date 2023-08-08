package com.example.batchprocessing.batch.reader;

import com.example.batchprocessing.Person;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

public class PersonDBCursorReader {
    private static final int chunkSize = 1000;

    private DataSource dataSource;

    public PersonDBCursorReader(DataSource dataSource) {
        this.dataSource= dataSource;
    }

    public JdbcCursorItemReader<Person> reader() {
        return new JdbcCursorItemReaderBuilder<Person>()
                .name("personCursorReader")
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Person.class))
                .sql("SELECT person_id, first_name, last_name from BAT_TEST_PERSON")
                .build();
    }
}
