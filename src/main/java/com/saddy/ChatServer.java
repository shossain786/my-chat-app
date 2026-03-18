package com.saddy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    static List<PrintWriter> clients = new ArrayList<PrintWriter>();

    static void main() throws IOException {
        ServerSocket server = new ServerSocket(1234);
        IO.println("############# Server Started! #############");
        while (true) {
            Socket client = server.accept();
            IO.println("Client connected: " + client.getInetAddress().getHostName());
            new Thread(new ClientHandler(client)).start();
        }
    }

    static class ClientHandler implements Runnable {
        Socket socket;
        BufferedReader in;
        PrintWriter out;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            clients.add(out);
        }

        @Override
        public void run() {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    IO.println("Client received: " + msg);

                    for (PrintWriter client : clients) {
                        client.println(msg);
                    }
                }
            } catch (IOException e) {
                IO.println("Client disconnected: " + e.getMessage());
            }
        }
    }
}
