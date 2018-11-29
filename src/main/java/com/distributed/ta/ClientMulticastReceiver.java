package com.distributed.ta;

import com.distributed.common.MulticastReceiver;

public class ClientMulticastReceiver extends MulticastReceiver {
    private NodeData ownNode = NodeData.getInstance();

    public ClientMulticastReceiver(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void proccessIncomingData(String data) {
        informeerNieuweNode(data);
    }


    public void informeerNieuweNode(String bericht) {


//        String name =  bericht.substring(1,bericht.length()-1);
//        String[] splits = name.split(",");
//        String newName = splits[0];
//        String newIP   = splits[0];
//        int newHash = Math.abs(newName.hashCode()) % 32768;
//
//        if ((Main.hashName < newHash) && (newHash < ownNode.getNextHash())) {
//
//            String previousMessage = "pre,"+ownNode.getHash()+","+ownNode.getNextHash()+","+ownNode.getIpAddress()+","+ownNode.getNextIP();
//            ownNode.setNextHash(newHash);                                                     //update nextNode met de hash van nieuwe node
//            UnicastPublisher previous = new UnicastPublisher(previousMessage,newIP);         //Stuur bericht "pre,"+hashName+","+nextHash naar new node met unicast
//            previous.run();
//        } else if ((ownNode.getPreviousHash() < newHash) && (newHash < Main.hashName)) {
//
//            ownNode.setPreviousHash(newHash);
//        }
    }
}
