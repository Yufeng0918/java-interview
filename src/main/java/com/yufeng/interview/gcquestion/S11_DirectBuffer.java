package com.yufeng.interview.gcquestion;

import java.nio.ByteBuffer;

public class S11_DirectBuffer {

    public static void main(String[] args) {

        System.out.println("maxDirectMemory:" + (sun.misc.VM.maxDirectMemory() / (double) 1024/1024) + " MB");

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 1024 * 1024);
    }
}
