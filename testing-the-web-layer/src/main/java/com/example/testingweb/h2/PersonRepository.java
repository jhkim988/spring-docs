package com.example.testingweb.h2;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public class PersonRepository {
    @Autowired
    private SqlSession sqlSession;
    public Person findById(Long id) {
        return sqlSession.selectOne("mapper.Person.findById", id);
    }

    public void add(Person person) {
        sqlSession.insert("mapper.Person.add", person);
    }
}
