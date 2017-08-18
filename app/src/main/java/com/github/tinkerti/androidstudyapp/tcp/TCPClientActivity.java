package com.github.tinkerti.androidstudyapp.tcp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.tinkerti.androidstudyapp.R;
import com.github.tinkerti.androidstudyapp.tcp.service.TCPServerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by tiankui on 8/16/17.
 */

public class TCPClientActivity extends Activity {

    private Socket clientSocket;
    private PrintWriter printWriter;
    private EditText inputContentView;
    private TextView receiveMessageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_tcp_client);
        receiveMessageView = (TextView) findViewById(R.id.tv_receive_message);
        inputContentView = (EditText) findViewById(R.id.et_input_content);
        Button sendButton = (Button) findViewById(R.id.btn_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputContentView.getText().toString();
                if (!TextUtils.isEmpty(message) && printWriter != null) {
                    printWriter.println(message);
                    inputContentView.setText("");
                    receiveMessageView.setText(receiveMessageView.getText() + "\n" + message);
                }
            }
        });

        Intent intent = new Intent(this, TCPServerService.class);
        startService(intent);

        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();

    }

    private void connectTCPServer() {
        Socket socket = null;
        try {
            socket = new Socket("10.0.2.2", 8688);
            clientSocket = socket;
            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()) {
                String msg = bufferedReader.readLine();
                if (msg != null) {
                    receiveMessageView.setText(receiveMessageView.getText() + "\n" + msg);
                }
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    private void test(){

    }
}
