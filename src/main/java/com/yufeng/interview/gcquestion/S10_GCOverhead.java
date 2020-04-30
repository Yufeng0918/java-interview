package com.yufeng.interview.gcquestion;

import java.util.ArrayList;
import java.util.List;

public class S10_GCOverhead {

    public static void main(String[] args) {
        int  i = 0;

        List<String> list = new ArrayList<>();

        while (true) {
            list.add(String.valueOf(++i).intern());
        }
    }

}
