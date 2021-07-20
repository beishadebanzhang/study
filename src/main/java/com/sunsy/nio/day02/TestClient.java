package com.sunsy.nio.day02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class TestClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8080));
        channel.write(Charset.defaultCharset().encode("1234567890abcdegc"));
        // channel.close();
        System.in.read();
    }
}
