package com.sunsy.nio.day01;

import java.nio.ByteBuffer;

/**
 * 数据的内存缓冲区
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        // 获取ByteBuffer
        // 在堆中分配内存，分配速度快，读写效率低，可能受gc影响，复制移动
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        // 使用直接内存，分配速度慢，读写效率高，可能存在内存泄露，少复制一次
        ByteBuffer buffer2 = ByteBuffer.allocateDirect(10);


    }
}
