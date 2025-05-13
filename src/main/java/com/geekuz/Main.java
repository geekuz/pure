package com.geekuz;

public class Main {
    public static void main(String[] args) {
        try {
            PureServer server = new PureServer(8080);
            server.start();
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}