package com.geekuz;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PureServer {
    private final int port;
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private boolean running = false;

    public PureServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        System.out.println("Server started on port " + port);
        
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleConnection(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }

    private void handleConnection(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream()
        ) {
            // Read the request
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            // Parse the request line
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];
            
            // Skip headers
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // Skip headers
            }
            
            // Handle the request
            if (method.equals("GET") && path.equals("/api/hello")) {
                String response = "{\"message\": \"Hello, World!\"}";
                String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                     "Content-Type: application/json\r\n" +
                                     "Content-Length: " + response.length() + "\r\n" +
                                     "\r\n" +
                                     response;
                out.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            } else {
                String notFound = "Not Found";
                String httpResponse = "HTTP/1.1 404 Not Found\r\n" +
                                     "Content-Type: text/plain\r\n" +
                                     "Content-Length: " + notFound.length() + "\r\n" +
                                     "\r\n" +
                                     notFound;
                out.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public void stop() {
        running = false;
        threadPool.shutdown();
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server: " + e.getMessage());
        }
    }
}