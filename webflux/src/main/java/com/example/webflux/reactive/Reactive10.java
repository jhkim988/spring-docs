package com.example.webflux.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.logging.Level;

public class Reactive10 {
    private static Logger logger = LoggerFactory.getLogger(Reactive10.class);

    /* 로깅과 체크포인트 */
    public static void main (String[] args) {
        Flux.just(1, 2, 3, 4, 5)
                .log() // 리액터의 동작을 자세히 보고 싶을 때 .log 를 이용한다.
                .map(x -> x * 2)
                .subscribe(x -> logger.info("next: {}", x));

        Flux.just(1, 2, 3, 4, 5)
                .log(null, Level.FINE) // 로그 레벨 설정
                .map(x -> x * 2)
                .subscribe(x -> logger.info("next: {}", x));

        Flux.just(1, 2, 3, 4, 5)
                .log("MyLog", Level.FINE) // 특정 로거 지정
                .map(x -> x * 2)
                .subscribe(x -> logger.info("next: {}", x));

        /* checkpoint 를 이용하여 어디서 Exception 이 발생하는지 쉽게 파악한다. */
        Flux.just(1, 2, 3, -1, -2, -3)
                .map(x -> x+2)
                .checkpoint("MAP1")
                .map(x -> 10 / x)
                .checkpoint("MAP2")
                .subscribe(x -> logger.info("next: {}", x), err -> err.printStackTrace());
    }
}
