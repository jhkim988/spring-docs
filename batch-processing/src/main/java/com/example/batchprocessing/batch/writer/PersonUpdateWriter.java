package com.example.batchprocessing.batch.writer;

import com.example.batchprocessing.Person;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;

public class PersonUpdateWriter {
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("Update bat_test_person set first_name = :first_name, last_name = :last_name where person_id = :person_id")
                .dataSource(dataSource)
                .build();
    }
}
