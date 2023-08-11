package com.example.webflux.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class Reactive02 {
    public static void main(String[] args) {
        /* 시퀀스 생성방법: just */
        Flux.just(1, 2, 3);
        Flux.just(); // 데이터 없이 Complete 신호만 발생한다.
//        Flux.just(null); // Null Pointer Exception.
        Flux.empty(); // just(null) 을 사용하지 말고, empty() 를 사용한다.
        Mono.justOrEmpty(Optional.of(1)); // 값이 있을 수도 있고 없을 수도 있는 Mono 를 생성할 때는 justOrEmpty 와 Optional 을 이용한다.
        Flux.range(11, 5); // start 부터 count 개수만큼 정수 생성

        /* 시퀀스 생성 방법: generate */
        Consumer<SynchronousSink<Integer>> randGen = new Consumer<>() {
            private int emitCount = 0;
            private Random rand = new Random();

            @Override
            public void accept(SynchronousSink<Integer> sink) {
                emitCount++;
                int data = rand.nextInt(100) + 1;
                System.out.println("Generator sink next " + data);
                sink.next(data);
                if (emitCount == 10) {
                    System.out.println("Generator sink complete");
                    sink.complete();
                }
            }
        };

        Flux.generate(randGen).subscribe(new BaseSubscriber<Integer>() {
            private int receivedCount = 0;

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscriber#onSubscribe");
                System.out.println("\nSubscriber request first 3 items");
                request(3);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("Subscriber#onNext: " + value);
                receivedCount++;
                if (receivedCount % 3 == 0) {
                    System.out.println("\nSubscriber request next 3 items");
                    request(3);
                }
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("Subscriber#onComplete");
            }
        });

        Flux.generate(
                () -> {
                    /* Initial State return */
                    return 0;
                }
                , (state, sink) -> {
                    /* Next State return */
                    sink.next(3 * state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                }
        );
    }
}
