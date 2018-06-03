package com.example.phuc.iot_smart_home_v2.components;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class GlobalSocket extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.URL_SOCKET);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getmSocket() {
        return mSocket;
    }
}
