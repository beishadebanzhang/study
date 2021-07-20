package com.sunsy.netty.day01;

import java.util.concurrent.TimeUnit;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * EventLoop 事件循环对象
 *  本质是一个单线程执行器（同时维护了一个selector），里面的run方法源源不断的处理channel的io事件
 *  继承了ScheduleExecutorService，因此包含其全部方法
 *  继承了netty的OrderedEventExecutor
 *      1.提供了boolean inEventLoop判断一个线程是否属于此EventLoop
 *      2.提供了parent方法来查看自己所属EventLoopGroup
 *
 * EventLoopGroup 事件循环组
 *      一组EventGroup，channel会调用register绑定其中一个EventGroup，后续这个channel的io操作事件
 *  就由此EventGroup处理，保证了io事件处理的线程安全性
 *      继承了netty的EventExecutorGroup
 *          1.实现了iterator接口提供遍历EventGroup的能力
 *          2.next方法获取下一个EventGroup
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        // 创建EventGroup
        NioEventLoopGroup group = new NioEventLoopGroup(2);

        // 会有轮询和负载均衡
        // System.out.println(group.next());
        // System.out.println(group.next());
        // System.out.println(group.next());

        // 执行普通任务
        // group.execute(() -> {
        //     try {
        //         Thread.sleep(3000);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        //     log.debug("common thread");
        // });

        group.scheduleAtFixedRate(()->{
            log.debug("schedule thread");
        }, 1, 1, TimeUnit.SECONDS);

        log.debug("main thread");
    }
}
