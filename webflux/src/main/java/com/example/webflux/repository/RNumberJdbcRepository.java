package com.example.webflux.repository;

import com.example.webflux.model.RNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public class RNumberJdbcRepository {
    @Autowired
    private SqlSession sqlSession;
    public List<RNumber> findAll() {
        return sqlSession.selectList("findAll");
    }
}
