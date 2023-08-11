package com.example.webflux.reactive;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Reactive06 {
    /* Thread Scheduling */
    public static void main(String[] args) {
        /* map, flatMap, subscribe 모두 메인스레드에서 실행된다. */
        Flux.range(1, 3)
                .map(x -> {
                    print(x + "");
                    return x + 2;
                })
                .flatMap(x -> {
                    print("flatMap " + x + " to Flux.range(" + 1 + "," + x + ")");
                    return Flux.range(1, x);
                })
                .subscribe(x -> print("next - " + x ));

        /* publishOn 을 이용하여 별도 스레드에서 처리할 수 있다. */
        Flux.range(1, 3)
                .map(x -> {
                    print(x + "");
                    return x + 2;
                })
                .publishOn(Schedulers.newBoundedElastic(1, 2, "PUB"), 2) // 두 번째인자 prefetch 는 신호를 처리하기 전에 미리 가져올 데이커 개수이다.
                .flatMap(x -> {
                    print("flatMap " + x + " to Flux.range(" + 1 + "," + x + ")");
                    return Flux.range(1, x);
                })
                .subscribe(x -> print("next - " + x ));

        /* doOnSubscribe 을 이용하여 Subscriber 가 시퀀스에 대한 request 신호를 별도 스케쥴러로 처리한다. */
        Flux.range(1, 3)
                .subscribeOn(Schedulers.newBoundedElastic(1, 2, "SUB")) // subscribeOn 은 시퀀스 생성 뒤에 바로 지정한다. publishOn 뒤에 지정하면 적용되지 않는다.
                .map(x -> {
                    print(x + "");
                    return x + 2;
                })
                .publishOn(Schedulers.newBoundedElastic(1, 2, "PUB"), 2)
                .flatMap(x -> {
                    print("flatMap " + x + " to Flux.range(" + 1 + "," + x + ")");
                    return Flux.range(1, x);
                })
                .subscribe(x -> print("next - " + x ));

        /*
         * Scheduler 종류
         * immediate: 현재 스레드
         * single: 스레드가 한 개인 스레드풀을 이용해서 실행
         * elastic: 스레드풀 이용. block IO 처리할 때 적합하다. 데몬 스레드 생성
         * parallel: 고정 크기 스레드풀 이용. 병렬 작업에 적합
         *
         * single, elastic, parallel 은 데몬 스레드를 생성하므로, 메인 스레드가 종료되면 함께 종료된다.
         * threadCap: 생성할 스레드 최대 개수
         * queuedTaskCap: 더 이상 스레드를 생성할 수 없을 때, 태스크 큐의 최대 크기
         */

        /*
        * Flux.interval: 일정한 주기로 신호를 발생시킬 수 있다. 발생 순서에 따라 발생한 정수값을 1씩 증가시킨다.
        * parallel 을 이용하여 발생시킨다. 두 번째 인자로 다른 스케쥴러를 사용할 수 있다.
        */
    }
    public static void print(String str) {
        System.out.printf("[%s] %s\n", Thread.currentThread().getName() , str);
    }
}
