package com.yufeng.interview.gcquestion;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class S06_ReferenceQueue {

    public static void main(String[] args) throws InterruptedException {

        Object o1 = new Object();
        ReferenceQueue referenceQueue = new ReferenceQueue();
        WeakReference<Object> weakReference = new WeakReference<>(o1, referenceQueue);

        System.out.println(o1);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());

        o1 = null;
        System.gc();
        Thread.sleep(500);

        System.out.println(o1);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll());
    }
}
