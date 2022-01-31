package com.geekbrains.cloud.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BuffersExample {

    private static final Logger LOG = LoggerFactory.getLogger(BuffersExample.class);

    public static void main(String[] args) {
        ByteBuffer buf = ByteBuffer.allocate(30);
        buf.putChar('H');
        buf.putChar('e');
        buf.flip();
        System.out.println(buf.getChar());
        System.out.println(buf.getChar());
        buf.rewind();
        System.out.println(buf.getChar());
        System.out.println(buf.getChar());

        buf.clear();
        buf.put("Hello world".getBytes(StandardCharsets.UTF_8));
        buf.flip();
        while (buf.hasRemaining()) {
            byte b = buf.get();
            System.out.print((char) b);
        }
        System.out.println();
        LOG.debug("Wow");
        LOG.info("Inf");

    }
}
