package com.sunsy.nio.day02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiThreadTest {
    public static void main(String[] args) throws IOException {

        Thread.currentThread().setName("boss");

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT, null);

        ssc.bind(new InetSocketAddress(8080));
        Worker worker = new Worker("worker-01");

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {

                    SocketChannel sc = ssc.accept();
                    log.debug("connect...{}", sc.getRemoteAddress());
                    sc.configureBlocking(false);
                    log.debug("before connect...{}", sc.getRemoteAddress());
                    worker.register(sc);
                    log.debug("after connect...{}", sc.getRemoteAddress());
                }
            }
        }

    }

    static class Worker implements Runnable {

        private Thread thread;

        private Selector selector;

        private String name;

        private volatile boolean start = false;

        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue();

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel channel) throws IOException {
            if (!start) {
                selector = Selector.open();
                thread = new Thread(this);
                thread.start();
                start = true;
            }

            queue.add(() -> {
                try {
                    channel.register(selector, SelectionKey.OP_READ, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            selector.wakeup();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    Runnable task = queue.poll();
                    if (null != task) {
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        // remove()???????????????selected-key set?????????
                        // selectedKeys????????????remove??????????????????????????????????????????????????????????????????????????????????????????????????????
                        iterator.remove();
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            channel.configureBlocking(false);
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            log.debug("before read...{}", channel.getRemoteAddress());
                            try {
                                int index = channel.read(buffer);
                                System.out.println(index);
                                if (index == -1) {
                                    log.debug("?????????????????????");
                                    // cancel()??????????????????selector.select()??????
                                    // key set???selected-key set???cancelled-key set????????????
                                    key.cancel();
                                } else {
                                    log.debug("after read...{}", channel.getRemoteAddress());
                                    buffer.flip();
                                    log.debug("buffer = {}", Charset.defaultCharset().decode(buffer).toString());

                                }
                            } catch (Exception e) {
                                log.debug("?????????????????????");
                                key.cancel();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
    // todo ?????????
    // todo IO??????/?????????/???????????????/????????????
    // todo java????????????io