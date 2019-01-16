package com.distributed.ta;

import com.distributed.common.*;

import java.util.HashMap;
import java.util.Map;

public class ClientMulticastReceiver extends MulticastReceiver {
    private NodeData ownNode = NodeData.getInstance();

    public ClientMulticastReceiver(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void proccessIncomingData(String data) {

        informNewNode(data);
    }


    public void informNewNode(String bericht) {


        String name =  bericht.substring(1,bericht.length()-1);    //accolades wegdoen
        String[] splits = name.split(",");                        //nu hebben we hash nieuuwe node en IP nieuwe node
        int newHashInt = Integer.parseInt(splits[0]);
        String newIP = splits[1];
        Node newNode = new Node(newHashInt, newIP);

        if ((ownNode.getThisNode().getHash() < newHashInt) && (newHashInt < ownNode.getNextNode().getHash())) {
            ClientComm.setPrevNeigbhour(ownNode.getThisNode(), newNode);
            ClientComm.setNextNeighbour(ownNode.getNextNode(), newNode);
            updateFiles(newHashInt, newNode);

        } else if ((ownNode.getPrevNode().getHash() < newNode.getHash()) && (newNode.getHash() < ownNode.getThisNode().getHash())) {
            ClientComm.setPrevNeigbhour(newNode, ownNode.getThisNode());
           }
    }

}
