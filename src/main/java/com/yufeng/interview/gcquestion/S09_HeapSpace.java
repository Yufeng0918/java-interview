package com.yufeng.interview.gcquestion;

import java.util.Random;

public class S09_HeapSpace {

    public static void main(String[] args) {

        String str = "sdfsfdsafdsaf";

        while (true) {
            str += str + new Random().nextInt(111111111) + new Random().nextInt(222222);
            str.intern();
        }
    }
}
