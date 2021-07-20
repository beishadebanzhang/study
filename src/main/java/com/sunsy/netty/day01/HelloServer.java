package com.sunsy.netty.day01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class HelloServer {
    public static void main(String[] args) {
        // ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求
        new ServerBootstrap()
            // 利用这个方法设置EventLoopGroup,Boos线程和Worker线程就是同一个
            // eventLoop可理解为处理channel的线程，一旦负责了某个channel，就会绑定
            // io操作绑定，handler中的工序可以交给其他线程，工序按pipeline顺序执行
            // eventLoop既可以进行io操作，也可以任务处理，用队列存放任务（普通/定时任务）
            .group(new NioEventLoopGroup())
            // 利用这个方法设置EventLoopGroup,Boos线程和Worker线程就是两个
            // .group(new NioEventLoopGroup() /*boss*/, new NioEventLoopGroup() /*worker*/)
            // 设置channel类型，工厂类会使用到并以此创建实例
            .channel(NioServerSocketChannel.class)
            // 设置accept事件时初始化socketChannel处理
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new StringDecoder());
                    // handler分为Inbound入站和Outbound出站两种
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println(msg);
                        }
                    });
                }
            }).bind(8080);
    }
}
