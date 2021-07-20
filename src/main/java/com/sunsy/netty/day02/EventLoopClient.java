package com.sunsy.netty.day02;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new StringEncoder());
                }
            })
            // connect为异步非阻塞方法，main发起了调用，真正执行connect的是nio线程
            .connect(new InetSocketAddress("localhost", 8080));

        // 使用sync方法同步处理结果
        // 不执行sync方法，channel并不会报空指针，channel已创建未初始化，所以无法发送数据
        // channelFuture.sync();
        // Channel channel = channelFuture.channel();
        // channel.writeAndFlush("hello");

        // 使用addListener(回调对象)方法异步处理结果
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            Channel channel = channelFuture1.channel();
            log.debug("channel = {}", channel);
            channel.writeAndFlush("hello");
        });


    }
}
