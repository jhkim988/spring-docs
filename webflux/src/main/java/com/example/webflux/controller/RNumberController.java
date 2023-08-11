package com.example.webflux.controller;

import com.example.webflux.model.RNumber;
import com.example.webflux.repository.RNumberJdbcRepository;
import com.example.webflux.repository.RNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RestController
public class RNumberController {
    @Autowired
    private RNumberRepository rNumberRepository;

    @Autowired
    private RNumberJdbcRepository rNumberJdbcRepository;
    @GetMapping(value="/r-number/webflux", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<RNumber> rNumberFlux() {
        return rNumberRepository.findAll();
    }

    @GetMapping(value="/r-number/webmvc")
    public List<RNumber> rNumberMVC() {
        return rNumberJdbcRepository.findAll();
    }
}
