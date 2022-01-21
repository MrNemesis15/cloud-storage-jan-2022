package com.geekbrains.cloud.nio;

import java.nio.channels.SocketChannel;

public interface MessageCallback {

    void onMessageReceived(String message, SocketChannel channel);

}
