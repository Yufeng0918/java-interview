package com.yufeng.interview.gcquestion;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class S07_PhantomReference {

    public static void main(String[] args) throws InterruptedException {


        Object o1 = new Object();
        ReferenceQueue referenceQueue = new ReferenceQueue();
        PhantomReference<Object> phantomReference = new PhantomReference<>(o1, referenceQueue);

        System.out.println(o1);
        System.out.println(phantomReference.get());
        System.out.println(referenceQueue.poll());

        o1 = null;
        System.gc();
        Thread.sleep(500);

        System.out.println(o1);
        System.out.println(phantomReference.get());
        System.out.println(referenceQueue.poll());
    }
}
