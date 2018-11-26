package com.distributed.ta;


import com.distributed.common.MulticastReceiver;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Connect {



    //Naam client moet in een file beschikbaar zijn en moet maar een keer ingegeven worden bij initial setup
    public static int hashName;
    String nodeName = "Albin";
    int port;
    String ip;


    public Connect(String ip, int port) {
       this.ip = ip;
       this.port = port;
    }





    public static MulticastReceiver receive = new MulticastReceiver("224.0.0.251",3000);


    //stuur multicast naar iedereen
    public void DiscoverMe() throws IOException {
        Scanner input = new Scanner(System.in);
        MulticastPublisher publisher = new MulticastPublisher();
        String name = input.next();
        hashName = Math.abs(name.hashCode()) % 32768;
        publisher.multicast(name +","+ Inet4Address.getLocalHost().getHostAddress());
        receive.run();
    }


    //listen for multicast messages
    public void MulticastListner(){

        MulticastSocket socket = null;
        byte[] buf = new byte[256];

        while(true) {
            try {
                socket = new MulticastSocket(port);
                InetAddress group = InetAddress.getByName(ip);
                socket.joinGroup(group);
                while (true) {
                    System.out.println("debug");
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received = new String(
                            packet.getData(), 0, packet.getLength());
                    //System.out.println("received: " + received);
                    new ReceivedData(received);
                    if ("end".equals(received)) {
                        break;
                    }
                }
                socket.leaveGroup(group);
                socket.close();
            } catch (Exception e) {
            }
        }
    }





}
