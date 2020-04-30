package com.yufeng.interview.gcquestion;

import java.util.HashMap;
import java.util.WeakHashMap;

public class S05_WeakHashMap {

    public static void main(String[] args) {

        myWeakHasMap();

    }


    private static void myHasMap() {

        HashMap<Integer, String> map = new HashMap<>();
        Integer key = new Integer(1);
        map.put(key, "HashMap");
        System.out.println(map);

        key = null;
        System.out.println(map);

        System.gc();
        System.out.println(map.size());
    }


    private static void myWeakHasMap() {

        WeakHashMap<Integer, String> map = new WeakHashMap<>();
        Integer key = new Integer(2);
        map.put(key, "WeakHashMap");
        System.out.println(map);

        key = null;
        System.out.println(map);

        System.gc();
        System.out.println(map.size());
    }




}
