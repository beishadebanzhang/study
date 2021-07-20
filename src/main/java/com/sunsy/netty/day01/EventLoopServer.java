package com.sunsy.netty.day01;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
// todo group如何换人
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        // 细分2：较为耗时的操作可以分给其他循环事件对象处理
        DefaultEventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
            // 细分1：
            // boss只负责ServerSocketChannel的accept事件,无需设置线程数，因为ServerSocketChannel只有一个
            // worker只负责SocketChannel的读写事件，可根据机器cpu核数设置线程数
            .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>(){
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast("hand1er-01", new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug(buf.toString(Charset.defaultCharset()));
                            ctx.fireChannelRead(msg);
                        }
                    }).addLast(group, "handler-02", new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            log.debug(buf.toString(Charset.defaultCharset()));
                        }
                    });
                }
            })
            .bind(8080);


    }
}
