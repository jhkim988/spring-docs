package com.example.webflux.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Random;

public class Reactive07 {
    /* 병렬처리 */
    public static void main(String[] args) {
        Flux.range(1, 100)
                .parallel(2) // 작업 레일을 2개로 나눈다.
                .runOn(Schedulers.newParallel("PAR", 2)) // 각 레일을 병렬로 실행한다.
                .map(x -> {
                    int sleepTime = nextSleepTime(x % 2 == 0 ? 50 : 100, x%2 == 0 ? 150 : 300);
                    sleep(sleepTime);
                    return String.format("%02d", x);
                })
                .subscribe(x -> print(x));

        /* 레일 수 보다 스레드 수가 많으면, 스레드들이 앞의 레일을 먼저 처리한다. 이후에 레일의 데이터 개수에 따라 스케줄러가 선택하는 레일이 달라진다. */

        /* Mono.zip 으로 병렬처리 */
        Mono m1 = Mono.just(1).map(x -> {
            print("1 sleep");
            sleep(1500);
            return x;
        }).subscribeOn(Schedulers.parallel());

        Mono m2 = Mono.just(2).map(x -> {
            print("2 sleep");
            sleep(3000);
            return x;
        }).subscribeOn(Schedulers.parallel());

        Mono m3 = Mono.just(3).map(x -> {
            print("3 sleep");
            sleep(2000);
            return x;
        }).subscribeOn(Schedulers.parallel());

        Mono.zip(m1, m2, m3).subscribe(tup -> print("next {" + tup + "}"));
    }
    public static int nextSleepTime(int lo, int hi) {
        Random random = new Random();
        return random.nextInt(lo, hi);
    }

    public static void print(String str) {
        System.out.printf("[%s] %s\n", Thread.currentThread().getName(), str);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            // do nothing
        }
    }
}
