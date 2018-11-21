package com.practicum;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.practicum.Main.hashName;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    int port;
    String ip;
    int vorige = 0;
    int volgende =32768;

    public MulticastReceiver(String ip, int port){
        this.port = port;
        this.ip = ip;
    }
    Map<String, String> Map = new HashMap<>();
    public void run() {
        try{
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
            zetInMap(received);
            System.out.println(Map);
            //functie
            if ("end".equals(received)) {
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();}catch (Exception e){

        }
    }
    public void zetInMap(String value){
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
    }
}
