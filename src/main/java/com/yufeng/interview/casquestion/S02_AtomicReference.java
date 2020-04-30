package com.yufeng.interview.casquestion;

import java.util.concurrent.atomic.AtomicReference;

public class S02_AtomicReference {

    public static void main(String[] args) {

        User u1 = new User("z3", 22);
        User u2 = new User("l3", 25);

        AtomicReference<User> atomicReference = new AtomicReference();
        atomicReference.set(u1);

        System.out.println(atomicReference.compareAndSet(u1, u2) + " " + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(u1, u2) + " " + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(u2, u1) + " " + atomicReference.get().toString());
    }
}


class User{

    public String username;
    public int age;

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
