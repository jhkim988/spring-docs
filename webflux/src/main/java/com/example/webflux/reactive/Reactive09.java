package com.example.webflux.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class Reactive09 {
    /* window - 데이터를 묶음처리 */
    public static void main(String[] args) {
        Flux<Flux<Integer>> windowSeq = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).window(4);
        windowSeq.subscribe(seq -> {
            Mono<List<Integer>> monoList = seq.collectList();
            monoList.subscribe(list -> print(list.toString()));
        });

        /* skip */
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .window(4, 3)// 4개씩 3개마다 묶는다. 마지막 데이터 중복이 발생한다.
                .subscribe(seq -> {
                    Mono<List<Integer>> monoList = seq.collectList();
                    monoList.subscribe(list -> print(list.toString()));
                });
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .window(4, 5)// 4개씩 5개마다 묶는다. 하나가 누락된다.
                .subscribe(seq -> {
                    Mono<List<Integer>> monoList = seq.collectList();
                    monoList.subscribe(list -> print(list.toString()));
                });
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .window(Duration.ofMillis(500)) // 정해진 시간마다 묶는다.
                .subscribe(seq -> {
                    Mono<List<Integer>> monoList = seq.collectList();
                    monoList.subscribe(list -> print(list.toString()));
                });
        Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .window(Duration.ofMillis(500), Duration.ofMillis(400)) // 위와 마찬가지로 skip 설정. 데이터 중복 발생한다.
                .subscribe(seq -> {
                    Mono<List<Integer>> monoList = seq.collectList();
                    monoList.subscribe(list -> print(list.toString()));
                });
        /* 비슷하게 windowUntil, windowWhile 등을 이용할 수 있다. */
        /* buffer 는 window 와 비슷하지만, Flux 가 아닌 List 로 묶는다는 점이 다르다. */
    }

    public static void print(String str) {
        System.out.printf("[%s] %s\n", Thread.currentThread().getName(), str);
    }
}
