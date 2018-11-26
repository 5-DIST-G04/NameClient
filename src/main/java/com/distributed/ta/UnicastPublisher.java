package com.distributed.ta;

import java.io.*;
import java.net.*;

public class UnicastPublisher {
    private String hostName;
    private int portNumber;
    private String message;

    public UnicastPublisher (String message, String hostname) {
        this.hostName = hostname;
        this.message = message;
        this.portNumber = 4444;
    }

    public void run() {

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))

        ) {

            out.println(message);

        } catch(UnknownHostException e){
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch(IOException e){
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }

    }
}