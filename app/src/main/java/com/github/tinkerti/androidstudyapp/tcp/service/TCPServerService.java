package com.github.tinkerti.androidstudyapp.tcp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tiankui on 8/16/17.
 */

public class TCPServerService extends Service {

    boolean isServiceDestroyed = false;

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8688);
            } catch (Exception e) {

            }

            while (!isServiceDestroyed) {
                try {
                    final Socket client = serverSocket.accept();
                    responseClient(client);
                } catch (Exception e) {

                }
            }

        }
    }

    private void responseClient(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        out.print("欢迎来到聊天室！");
        while (!isServiceDestroyed) {
            String str = in.readLine();
            if (str == null) {
                break;
            }

            String msg = "test message";
            out.println(msg);
        }

        in.close();
        out.close();
        socket.close();
    }
}
