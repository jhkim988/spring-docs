package com.example.batchprocessing.batch.reader;

import com.example.batchprocessing.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class PersonDBPagingReader {
    private static final Logger logger = LoggerFactory.getLogger(PersonDBPagingReader.class);
    private static final int chunkSize = 1000;
    private DataSource dataSource;

    public PersonDBPagingReader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcPagingItemReader<Person> reader() {
        try {
            return new JdbcPagingItemReaderBuilder<Person>()
                    .name("personPagingReader")
                    .fetchSize(chunkSize)
                    .dataSource(dataSource)
                    .rowMapper(new BeanPropertyRowMapper<>(Person.class))
                    .queryProvider(createQueryProvider())
                    .build();
        } catch (Exception e) {
            logger.error("createQueryProvider Error", e);
            return null;
        }
    }

    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
        factory.setDataSource(dataSource);
        factory.setSelectClause("person_id, first_name, last_name");
        factory.setFromClause("from bat_test_person");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("person_id", Order.ASCENDING);
        factory.setSortKeys(sortKeys);
        return factory.getObject();
    }
}
