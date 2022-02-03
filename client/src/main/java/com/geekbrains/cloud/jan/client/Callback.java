package com.geekbrains.cloud.jan.client;

import com.geekbrains.cloud.jan.model.AbstractMessage;

import java.io.IOException;

public interface Callback {

    void onMessageReceived(AbstractMessage message) throws IOException;

}
