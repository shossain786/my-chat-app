package com.saddy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    static Map<String, PrintWriter> clients = new HashMap<>();

    static void main() throws IOException {
        ServerSocket server = new ServerSocket(1234);
        IO.println("############# Server Started! #############");
        while (true) {
            Socket socket = server.accept();
            IO.println("Client connected: " + socket.getInetAddress().getHostName());
            new Thread(new ClientHandler(socket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String username;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Enter your username: ");
            username = in.readLine();
            clients.put(username, out);
        }

        @Override
        public void run() {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    IO.println(username + " messaged: " + msg);

                    String fullMsg = username + ": " + msg;
                    for (PrintWriter client : clients.values()) {
                        client.println(fullMsg);
                    }
                }
            } catch (IOException e) {
                clients.remove(username);
                IO.println(username + " disconnected");
            }
        }
    }
}
