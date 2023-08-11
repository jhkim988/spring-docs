package com.example.webflux.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Reactive03 {
    public static void main(String[] args) {
        /* 시퀀스 생성: create */
        Flux.create((FluxSink<Integer> sink) -> {
            /* request: 데이터 요청 개수 */
            /* subscriber 요청과 상관없이 데이터를 발생시킬 수 있다. */
            sink.onRequest(request -> {
                for (int i = 1; i <= request; i++) {
                    sink.next(i);
                }
            });
        });

        Flux.create((FluxSink<Integer> sink) -> {
            /* 요청한 것보다 데이터를 더 많이 발생시킬 수 있다. */
            /* 기본적으로 버퍼에 저장했다가 다음 데이터 요청 때 전달한다. 해당 전략을 두 번째 인자로 설정할 수 있다. */
            sink.onRequest(request -> {
                for (int i = 1; i <= request+3; i++) {
                    sink.next(i);
                }
            });
            /*
            * back pressure 전략
            * IGNORE: subscriber 의 요청을 무시하고 데이터 발생시킴
            * ERROR: Exception 발생시킴
            * DROP: Subscriber 가 데이터 받을 준비가 되지 않았으면 데이터를 누락시킴
            * LATEST: 마지막 신호만 Subscriber 에 전달함
            * BUFFER: 버퍼에 저장했다가 다음 요청 시 전달(default)
            */
        }, FluxSink.OverflowStrategy.BUFFER);

        /* Stream, Iterable 를 Flux 로 전환 */
        Flux.fromStream(Stream.of(1, 2, 3));
        Flux.fromIterable(List.of(1, 2, 3));
    }
}
