package com.example.webflux.reactive;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.util.Random;

public class Reactive05 {
    /*
     * 에러처리: 데이터 발생 과정 중 에러가 발생할 때 에러 핸들링
     * 에러 신호를 받으면 종료한다.
     */
    public static void main(String[] args) {
        Flux.range(1, 10)
                .map(x -> {
                    if (x == 5) throw new RuntimeException("Exception");
                    return x;
                }).subscribe(
                        System.out::println,
                        ex -> System.err.println(ex.getMessage()),
                        () -> System.out.println("complete"));

        /* onErrorReturn: 에러가 발생할 때 사용할 값, 특정 에러 조건에 따라 값을 다르게 발생시킬 수도 있다. (Predicate<>, Class<>) */
        Flux.range(1, 10)
                .map(x -> {
                    if (x == 5) throw new RuntimeException("Exception");
                    return x;
                })
                .onErrorReturn(-1)
                .subscribe(System.out::println);

        /* onErrorResume: 에러 발생 시 다른 에러로 대체 */
        Random random = new Random();
        Flux.range(1, 10)
                .map(x -> {
                    int rand = random.nextInt(8);
                    if (rand == 0) throw new IllegalArgumentException("illegal args");
                    if (rand == 1) throw new IllegalStateException("illegal state");
                    if (rand == 2) throw new RuntimeException("runtime exception");
                    return x;
                }).onErrorResume(error -> {
                    if (error instanceof IllegalArgumentException) {
                        return Flux.just(21, 22);
                    }
                    if (error instanceof  IllegalStateException) {
                        return Flux.just(31, 32);
                    }
                    return Flux.error(error);
                }).subscribe(System.out::println);

        /* onErrorMap: 에러를 다른 에러로 변환한다. */
        Flux.range(1, 10)
                .map(x -> {
                    if (x == 5) throw new IllegalArgumentException("ill args");
                    return x;
                })
                .onErrorMap(err -> new RuntimeException("runtime Exception"))
                .subscribe(System.out::println);
        /* retry: 재시도 횟수를 지정할 수 있다. */
        Flux.range(1, 10)
                .map(x -> {
                    if (x == 5) throw new IllegalArgumentException("ill args");
                    return x;
                }).retry(1)
                .subscribe(System.out::println);
        /* retryWhen: 재시도 조건을 지정한다. */
        // Retry.max, Retry.fixedDelay, Retry.backoff 등
        Flux.range(1, 10)
                .map(x -> {
                    if (x <= 5) return x;
                    throw new IllegalStateException("illegal");
                }).retryWhen(Retry.max(10))
                .subscribe(System.out::println,
                        err -> System.err.println("에러 발생 " + err.getMessage()),
                        () -> System.out.println("complete"));
    }
}
