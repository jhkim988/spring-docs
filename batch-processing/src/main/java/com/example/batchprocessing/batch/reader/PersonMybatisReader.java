package com.example.batchprocessing.batch.reader;

import com.example.batchprocessing.Person;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.context.annotation.Configuration;

public class PersonMybatisReader {

    private SqlSessionFactory sqlSessionFactory;

    public PersonMybatisReader(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    public MyBatisPagingItemReader<Person> reader() {
        return new MyBatisPagingItemReaderBuilder<Person>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("mapper.PersonMapper.selectPerson")
                .build();
    }
}
