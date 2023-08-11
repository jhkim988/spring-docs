package com.example.webflux.reactive;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Reactive04 {
    /* 시퀀스 변환 */
    public static void main(String[] args) {
        /* map: 1:1 변환 */
        System.out.println("map");
        Flux.just("a", "ab", "abc")
                .map(str -> str.length())
                .subscribe(value -> System.out.printf("data - {%d}\n", value));

        /* flatMap: 1:N 변환 */
        System.out.println("flatMap");
        Flux.just(1, 2, 3)
                .flatMap(i -> Flux.range(1, i))
                .subscribe(value -> System.out.printf("data - {%d}\n", value));

        /* Filter */
        System.out.println("filter");
        Flux.range(1, 10)
                .filter(num -> num % 2 == 0)
                .subscribe(value -> System.out.printf("data - {%d}\n", value));

        /* defaultIfEmpty: 빈 시퀀스일 경우 기본값 사용 */
        System.out.println("defaultIfEmpty");
        Flux<Integer> empty = Flux.just();
        empty.defaultIfEmpty(1).subscribe(value -> System.out.printf("data - {%d}\n", value));

        /* switchIfEmpty: 빈 시퀀스일 경우 다른 시퀀스 사용 */
        System.out.println("switchIfEmpty");
        Flux<Integer> alter = Flux.just(1, 2, 3);
        empty.switchIfEmpty(alter).subscribe(value -> System.out.printf("data - {%d}\n", value));

        /* startWith: 특정 값으로 시작하도록 */
        System.out.println("startWith");
        alter.startWith(-1, 0).subscribe(value -> System.out.printf("data - {%d}\n", value));

        /* concatWithValues: 특정 값으로 끝나도록 */
        System.out.println("concatWithValues");
        alter.concatWithValues(4, 5).subscribe(value -> System.out.printf("data -{%d}\n", value));

        /* 지정한 개수/시간/조건에 해당하는 데이터만 유지 */
        System.out.println("take");
        Flux<Integer> seq = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // 지정된 개수
        seq.take(5).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // 지정된 시간
        seq.take(Duration.ofSeconds(1)).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // 마지막 개수
        seq.takeLast(5).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // true 를 리턴하는 동안
        seq.takeWhile(x -> x <= 5).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // 처음 true 를 리턴할 때까지
        seq.takeUntil(x -> x > 5).subscribe(value -> System.out.printf("data -{%d}\n", value));

        /* 지정한 개수/시간/조건에 대항하는 데이터를 스킵*/
        // 개수
        seq.skip(3).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // 시간
        seq.skip(Duration.ofSeconds(1)).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // true 를 리턴하는 동안
        seq.skipWhile(x -> x <= 3).subscribe(value -> System.out.printf("data -{%d}\n", value));
        // 처음 true 를 리턴할 때까지
        seq.skipUntil(x -> x > 7).subscribe(value -> System.out.printf("data -{%d}\n", value));

        /* concatWith: 시퀀스 연결 (연결한 순서대로) */
        Flux<String> tick1 = Flux.interval(Duration.ofSeconds(1)).map(tick -> tick+"초틱");
        Flux<String> tick2 = Flux.interval(Duration.ofMillis(700)).map(tick -> tick+"밀리초틱");
        tick1.concatWith(tick2).concatWith(tick1).subscribe(value -> System.out.printf("concatWith - {%s}\n", value));

        /* mergeWith: 시퀀스 연결 (데이터가 발생하는 순서대로) */
        tick1.mergeWith(tick2).subscribe(value -> System.out.printf("mergeWith -{%s}\n", value));

        /* zipWith: 시퀀스를 묶는다. (1번째, 1번째) -> (2번째, 2번째) -> ... */
        tick1.zipWith(tick2).subscribe(value -> System.out.printf("zipWith -{%s}\n", value));

        /* combineLatest: 시퀀스를 묶는데, 데이터가 발생하면, 다른 시퀀스의 최신값을 가지고 쌍을 만든다. */
        Flux.combineLatest(tick1, tick2, (a, b) -> "[" + a + ", " + b + "]")
                .subscribe(value -> System.out.printf("zipWith -{%s}\n", value));


        try {
            /* Flux.interval 은 메인 스레드가 종료되면 실행되지 않으므로 조치 필요 */
            Thread.sleep(10_000L);
        } catch (Exception ex) {
            /* do nothing */
        }
    }
}
