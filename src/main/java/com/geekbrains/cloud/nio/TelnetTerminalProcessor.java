package com.geekbrains.cloud.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
public class TelnetTerminalProcessor implements ClientProcessor {

    private static Path currentDir;

    public TelnetTerminalProcessor() {
        currentDir = Paths.get(System.getProperty("user.home"));
    }

    @Override
    public void onMessageReceived(String msg, SocketChannel channel) throws IOException {
        log.info("received: {}", msg);
        msg = msg.trim();
        if (msg.equals("ls")) {
            String listFilesResponse = Files.list(currentDir)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.joining("\n")) + "\n";
            channel.write(ByteBuffer.wrap(
                    listFilesResponse.getBytes(StandardCharsets.UTF_8)
                    )
            );
        } else if (msg.startsWith("cd")) {
            String dst = msg.split(" +")[1];
            if (dst.length() != 2){
                log.info ("error");
            }
            if (Files.isDirectory(currentDir.resolve(dst))) {
                currentDir = currentDir.resolve(dst);
            }
        }
        String prefix = currentDir.getFileName().toString() + "> ";
        channel.write(ByteBuffer.wrap(prefix.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void onClientDisconnected(SocketChannel channel) throws IOException {
        log.info("Client disconnected...");
    }

    @Override
    public void onClientAccepted(SocketChannel channel) throws IOException {
        channel.write(ByteBuffer.wrap(
                "Hello user. Welcome to our terminal\n\r".getBytes(StandardCharsets.UTF_8)
        ));
        log.info("Client accepted...");
        String prefix = currentDir.getFileName().toString() + "> ";
        channel.write(ByteBuffer.wrap(prefix.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void onExceptionCaught(Throwable ex) throws IOException {
        log.error("error: ", ex);
    }
}
