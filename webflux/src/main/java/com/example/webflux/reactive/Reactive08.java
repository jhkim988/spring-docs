package com.example.webflux.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Reactive08 {
    /* 집계 Aggregation */
    public static void main(String[] args) {
        /* List 으로 모으기 */
        Flux<Integer> someFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Mono<List<Integer>> mono = someFlux.collectList();
        mono.subscribe(x -> System.out.println(x));

        /* Map 으로 모으기 */
        // keyExtractor: 데이터에서 맵의 키를 제공하는 함수
        // valueExtractor: 데이터에서 맵의 값을 제공하는 함수
        // mapSupplier: 사용할 Map 객체를 제공(없으면 default 로 HashMap)
        Flux<Tuple2<Integer, String>> numTupFlux = Flux.just(
                Tuples.of(1, "하나"),
                Tuples.of(2, "둘"),
                Tuples.of(3, "셋"),
                Tuples.of(4, "넷"),
                Tuples.of(5, "다섯"));
        Mono<Map<Integer, Tuple2<Integer, String>>> numTupMapMono = numTupFlux.collectMap(x -> x.getT1());
        Mono<Map<Integer, String>> numLabelMapMono = numTupFlux.collectMap(x -> x.getT1(), x -> x.getT2());
        Mono<Map<Integer, String>> numLabelTreeMapMono = numTupFlux.collectMap(x -> x.getT1(), x -> x.getT2(), TreeMap<Integer, String>::new);

        /* MultiMap 으로 모으기 */
        Mono<Map<Integer, Collection<Integer>>> oddEventList = Flux.just(1, 2, 3, 4).collectMultimap(x -> x%2);
        oddEventList.subscribe(map -> System.out.println(map));

        /* count 로 개수 세기 */
        Mono<Long> countMono = Flux.just(1, 2, 3, 4).count();

        /* reduce 로 누적하기 */
        Mono<Integer> sum = Flux.just(1, 2, 3, 4, 5).reduce((acc, el) -> acc + el);

        /* scan 으로 누적하면서 값 생성하기 - reduce 의 중간값들을 생성한다. */
        Flux<Integer> seq = Flux.just(1, 2, 3, 4).scan((acc, el) -> acc * el);

        /* 데이터 조건 검사 */
        Mono<Boolean> all = Flux.just(1, 2, 3, 4).all(x -> x % 2 == 0);
        Mono<Boolean> any = Flux.just(1, 2, 3, 4).any(x -> x % 2 == 0);
        Mono<Boolean> hasElement = Flux.just(1, 2, 3, 4).hasElement(3); // find
        Mono<Boolean> hasElements = Flux.just(1, 2, 3, 4).hasElements(); // exist
    }
}
