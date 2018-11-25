package com.practicum;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

//import static javafx.application.Platform.exit;

public class Main {

    //data locaal opslaan
    public String nextNode, previouseNode;
    public int nextHash, previousHash;
    String nameServerIP = "192.168.1.2";


    public static MulticastReceiver receive = new MulticastReceiver("224.0.0.251", 3000);
    public static int hashName;
    public JSONService jsonService = new JSONService(nameServerIP);

    public static void main(String args[]) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("debug");
        MulticastPublisher publisher = new MulticastPublisher();
        System.out.println("enter the name of this node");
        String name = input.next();
        hashName = Math.abs(name.hashCode()) % 32768;
        Node ownNode = new Node(name, Inet4Address.getLocalHost().getHostAddress(), 0, 0, "", "");

        //bootstrap & discovery
        publisher.multicast(name + "," + Inet4Address.getLocalHost().getHostAddress());
        UnicastMultiServer unicastReceiver = new UnicastMultiServer();
        unicastReceiver.run(ownNode);
        if ((ownNode.getNextHash() != 0) && (ownNode.getPreviousHash() != 0) && (!ownNode.getNextIP().isEmpty()) && (!ownNode.getPreviousIP().isEmpty()))
            receive.run(ownNode);


        /*MulticastPublisher publish = new MulticastPublisher();
        System.out.println("Enter the server url:");
        String url = input.next();

        JSONService json = new JSONService(url);

        System.out.println("enter the name of this node");
        String name = input.next();
        try {
            json.SubmitName(name);
        } catch (StatusExeption e){
            System.out.printf("the server responded with statuscode: %d\n",e.getStatuscode());
            return;
        }

        while (true){
            System.out.println("Enter the file name you want the location of or enter quit to exit");
            String fileName = input.next();
            if (fileName.equals("quit"))
                break;
            FileLocation fileLocation;
            try {
                fileLocation = json.getFileLocation(fileName);
                System.out.println("the file: " + fileLocation.getFileName() + " is to be found at: " + fileLocation.getIpAddress());
            } catch (StatusExeption e){
                System.out.printf("the server responded with status code: %d\n",e.getStatuscode());
            }
        }

        try {
            json.RemoveName(name);
            System.out.println("the node was unregistered from the nameserver");
        } catch (StatusExeption e){
            System.out.printf("the server responded with status code %d\n",e.getStatuscode());
        }
*/
    }



   /* public void shutdown() throws StatusExeption {
       //ask nameserver for the ip of nextNode:
        NodeIP nodeIP = new JSONService(nameServerIP).getNodeIP(nextNode);

        //contact nextnode and ask to update his previouse node
        new JSONService(nodeIP.getIP()).setPreviouseNode(previouseNode);
        exit();

    }*/

    public void shutdown(Node ownNode) {
        String nextToPrevious = "shu," + hashName + "," + ownNode.getNextHash() + "," + ownNode.getNextIP();
        UnicastPublisher shutdownPreviousNode = new UnicastPublisher(nextToPrevious, ownNode.getPreviousIP());         //send id of next node to previous node
        shutdownPreviousNode.run();

        String previousToNext = "shu," + hashName + "," + ownNode.getPreviousHash() + "," + ownNode.getPreviousIP();
        UnicastPublisher shutdownNextNode = new UnicastPublisher(previousToNext, ownNode.getNextIP());         //send id of previous node to next node
        shutdownNextNode.run();

        String toNameserver = "shu," + hashName ;
        UnicastPublisher shutdownToServer = new UnicastPublisher(toNameserver, nameServerIP);         //send id of previous node to next node
        shutdownToServer.run();
    }


}






