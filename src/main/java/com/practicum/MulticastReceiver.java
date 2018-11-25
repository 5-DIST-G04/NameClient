package com.practicum;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.practicum.Main.hashName;
import static com.practicum.Main.receive;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    int port;
    String ip;


    public MulticastReceiver(String ip, int port){
        this.port = port;
        this.ip = ip;

    }
    //Map<String, String> Map = new HashMap<>();

    public void run(Node ownNode) {
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
                System.out.println("received: " + received);
                informeerNieuweNode(received,ownNode);


                //verwerkingNieuweNode(received);
                //zetInMap(received);
                //System.out.println(Map);
                //functie
                if ("end".equals(received)) {
                    break;
                }
            }


            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {

        }
    }


    public void informeerNieuweNode(String bericht, Node ownNode) {
        String name =  bericht.substring(1,bericht.length()-1);
        String[] splits = name.split(",");
        String newName = splits[0];
        String newIP   = splits[0];
        int newHash = Math.abs(newName.hashCode()) % 32768;

        if ((hashName < newHash) && (newHash < ownNode.getNextHash())) {

            String previousMessage = "pre,"+ownNode.getHash()+","+ownNode.getNextHash()+","+ownNode.getIpAddress()+","+ownNode.getNextIP();
            ownNode.setNextHash(newHash);                                                     //update nextNode met de hash van nieuwe node
            UnicastPublisher previous = new UnicastPublisher(previousMessage,newIP);         //Stuur bericht "pre,"+hashName+","+nextHash naar new node met unicast
            previous.run();
        } else if ((ownNode.getPreviousHash() < newHash) && (newHash < hashName)) {

            ownNode.setPreviousHash(newHash);
        }
    }


    }




   /* public void zetInMap(String value){
        value = value.substring(1, value.length()-1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value
            Map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
        berekenPositie();
    }
    public void berekenPositie(){
        Iterator it = Map.entrySet().iterator();
        int huidige = hashName;
        System.out.println("huidige=" + huidige);
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(huidige < (int)pair.getKey()  && (int)pair.getKey() < volgende){
                volgende = (int)pair.getKey();
            }
           // System.out.println(pair.getKey());
            it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("volgende = "+volgende);
    }*/



