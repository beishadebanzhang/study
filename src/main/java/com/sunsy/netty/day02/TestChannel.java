package com.sunsy.netty.day02;

/**
 * Channel的主要作用
 *  1.close()可以用来关闭channel
 *  2.closeFuture()用来处理channel的关闭
 *      sync方法的作用是同步等待channel关闭
 *      addListener方法是异步等待channel关闭
 *  3.pipeline()方法添加处理器
 *  4.write()方法写入数据
 *  5.writeAndFlush()方法将数据写入并刷出
 */
public class TestChannel {
    public static void main(String[] args) {

    }
}
