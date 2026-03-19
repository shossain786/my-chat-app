package com.saddy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 1234);

        // reading input from user terminal
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//        reading message form Server
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//        to send message to server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Thread to listen messages
        new Thread(() -> {
            try {
                String response;
                while ((response = serverInput.readLine()) != null) {
                    IO.println(response);
                }
            } catch (Exception e) {}
        }).start();

        // Send messages
        String msg;
        while ((msg = input.readLine()) != null) {
            out.println(msg);
        }
    }
}
