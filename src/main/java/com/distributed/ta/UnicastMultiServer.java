package com.distributed.ta;


import java.net.*;
import java.io.*;

public class UnicastMultiServer {
    //private static String fileName = "C:\\Users\\yvesk\\OneDrive\\school\\2018-2019\\Distributed systems\\practicum\\text.txt";
    public void run() {

        /*
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }*/

        int portNumber = 4444;//Integer.pars    eInt(args[0]);
        int threadNumber=1;

        try {
            ServerSocket serverSocket =
                    new ServerSocket(4444/*Integer.parseInt(args[0]*/);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                UnicastThread thread = new UnicastThread(clientSocket,threadNumber);
                thread.run();
                threadNumber++;
            }
        }

        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}