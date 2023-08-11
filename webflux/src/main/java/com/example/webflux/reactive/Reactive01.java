package com.example.webflux.reactive;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

public class Reactive01 {
    /*
     * Reactive Stream API
     * Stream: 시간에 따라 생성되는 data/event/signal
     * 3가지 Signal: onNext, onComplete, onError
     * onNext 에는 데이터를 담고, onComplete 는 스트림이 완료됐음을 의미, onError 는 에러가 발생했음을 의미한다.
     *
     * Publisher 로 스트림을 정의하고, Subscriber 로 발생한 신호를 처리한다.
     * Subscriber 가 Publisher 로부터 신호를 받는 것을 구독이라고 한다.
     */
    public static void main(String[] args) {
        /* Flux: 0개 이상의 데이터 (0..N) */
        /* Mono: 1개 이하의 데이터 (0..1) */
        /* just: 이미 존재하는 값을 사용해서 Flux/Mono 를 생성할 때 사용한다. */
        Flux<Integer> seq = Flux.just(1, 2, 3);
        seq.subscribe(value -> System.out.printf("데이터 - %d\n", value));

        Flux.just(1, 2, 3).doOnNext(i -> System.out.printf("doOnNext: %d\n", i)) // Subscriber 에 next 신호를 발생할 때 호출된다. 시퀀스가 생성된 시점에 실행되지 않는다.
                .subscribe(i -> System.out.printf("Received: %d\n", i));

        /*
         * Cold Sequence: 구독 시점부터 데이터를 새로 생성한다. (ex. API 호출)
         * Hot Sequence: 구독자 수에 상관없이 데이터를 생성한다. (ex. 센서 데이터)
         */

        seq.subscribe(new Subscriber<Integer>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("Subscriber.onSubscribe");
                this.subscription = s;
                this.subscription.request(1); // publisher 에 데이터를 1개 요청한다.
//                this.subscription.request(Long.MAX_VALUE); // 데이터를 끝까지 발생시킨다.
            }

            @Override
            public void onNext(Integer i) {
                System.out.printf("Subscriber.onNext: %d\n", i);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Subscriber.onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Subscriber.onComplete");
            }
        });
    }
}
