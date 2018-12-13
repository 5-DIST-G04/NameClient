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

    public void updateFiles(int newHashInt, Node newNode) {
        // hier gaan we alle elementen van de map filesLog af, maw alle files waar de node owner van is.
        for (Map.Entry<File,HashMap<Node,Integer> > entry : ownNode.getfileLog().entrySet()) {              //elke entry bestaat uit een file (=key) en de hashmap(=value) waarin dan weer enerzijds de nodes (=keys) te vinden zijn die deze file bezitten en een intger value (=value) die aangeeft of het om replicated, local of downloaded gaat.
            if ((newHashInt>entry.getKey().getHash()) && (newHashInt<ownNode.getThisNode().getHash())) {
                //zet de isFileOwner boolean op true
                //replicate file naar de nieuwe node, deze zal weten wat hij er mee moet doen zoals beschreven in methode "receiveFile" in klasse ClientNode
                //deze node blijft bezitter van replicated/local exemplaar van de file dus moet niks aan logbestand wijzigen, de ontvanger, zal het logbestand overnemen en zijn eigen er aan toevoegen.
                //verwijder de fileLog uit de hashmap van fileLogs op deze node, want hij is geen owner meer.
                //dit kan ik waarschijnlijk niet allemaal doen binnen deze for lus want entry kan ik niet gebruiken als file object?
                entry.getKey().setFileOwner(true);
                ClientComm.replicate(entry.getKey(), newNode);
                ownNode.getfileLog().remove(entry);                      //verwijder fileLog van deze file uit lijst van fileLog want hij is niet meer owner.
                //gaat die for lus nog correct runnen als ik binnen de lus een entry verwijder?
            }
        }
    }
}
