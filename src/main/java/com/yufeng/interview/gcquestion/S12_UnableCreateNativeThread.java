package com.yufeng.interview.gcquestion;

import java.util.concurrent.TimeUnit;

public class S12_UnableCreateNativeThread {

    public static void main(String[] args) {

        while (true) {
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
