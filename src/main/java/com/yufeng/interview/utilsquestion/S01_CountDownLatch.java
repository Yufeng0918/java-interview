package com.yufeng.interview.utilsquestion;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class S01_CountDownLatch {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(6);

        IntStream.range(1, 7).forEach(i -> {

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " leaving");
                countDownLatch.countDown();
            }, CountryEnum.forEachCountry(i).message).start();
        });

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " lock");
    }

}

enum CountryEnum {

    ONE(1, "QI"), TWO(2, "CHU"),THREE(3, "YAN"),FOUR(4, "ZHAO"),FIVE(5, "WEI"),SIX(6, "HAN");

    public Integer code;
    public String message;

    CountryEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CountryEnum forEachCountry(final int index) {

        CountryEnum[] myArray = CountryEnum.values();
        return Stream.of(myArray).filter(item -> item.code == index).findFirst().orElse(null);
    }
}
