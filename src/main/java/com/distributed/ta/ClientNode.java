package com.distributed.ta;


import com.distributed.common.*;
import com.distributed.common.File;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.*;
import java.net.Inet4Address;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class ClientNode {
    private NodeData nodeData = NodeData.getInstance();
    private HttpServer server;
    private MulticastReceiver receive = new ClientMulticastReceiver("224.0.0.251", 3000);
    private MulticastPublisher publisher = new MulticastPublisher("224.0.0.251" , 3000);

    public ClientNode(String name){
        try {
            nodeData.setThisNode(new Node(name, Inet4Address.getLocalHost().getHostAddress()));
        } catch (UnknownHostException e){
            System.out.println("check whether there is an internet connection");
            e.printStackTrace();
        }
    }

    public void Start(){
        StartGrizzlyContainer();
        StartMulticastReceiver();
        SendInitMulticast();
        VerifyLocalFiles();

    }

    public void Stop(){
        ServerComm.RemoveOwnNode();
        receive.stop();
        stopServer();
    }

    private void StartGrizzlyContainer(){
        server = startServer();
    }

    private void StartMulticastReceiver(){
        receive.start();
    }

    private void SendInitMulticast(){
        try {
            publisher.multicast( nodeData.getThisNode().getHash() + "," + nodeData.getThisNode().getIpAddress());
        } catch (IOException e) {
            System.out.println("The multicast failed");
            e.printStackTrace();
        }
    }

    private HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.distributed.ta package
        final ResourceConfig rc = new ResourceConfig().packages("com.distributed.ta");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(nodeData.getBaseUri()), rc);
    }

    private void stopServer(){
        server.stop();
    }


    private void VerifyLocalFiles () {
        File aDirectory = new File("src/main/java/com/distributed/data");
        String[] filesInDir = aDirectory.list();


        for ( int i=0; i<filesInDir.length; i++ )
        {

            replicateFile( filesInDir[i]);
        }
    }

    private void replicateFile(String s) {
        //vraag aan server voor de node waar de HAshNodeID <= hashFile
        File file = new File(s);
        file.getFileLog().put(nodeData.getThisNode(),0);        //local=0, replicated=1, downloaded=2
        nodeData.addLocal(file);
        int fileHash = file.getHash();
        Node replicationNode = ServerComm.getReplicationNode(fileHash);
        if (replicationNode==nodeData.getThisNode()) {
            file.setFileOwner(false);
            ClientComm.replicate(file, nodeData.getPrevNode());     //dit klopt nog niet want we moeten de echte file doorgegeven niet file die we zelf net hebben aangemaakt met zelfde naam
    }
        else {
            file.setFileOwner(true);
            ClientComm.replicate(file,replicationNode);
        }
    }

    private void receiveFile(File file, Node sender) {

        if (!file.isShutdown()) {
            nodeData.addReplicated(file);
            if (file.isFileOwner() == true)
                file.getFileLog().put(nodeData.getThisNode(), 1);
            nodeData.addfileLog(file);
        } else if (file.isShutdown()) {
            replicateFile(file.getName());
        }
    }


    private void shutDown() {
            for (Map.Entry<File, HashMap<Node,Integer>> entry : nodeData.getfileLog().entrySet()) {              //elke entry bestaat uit een file (=key) en de hashmap(=value) waarin dan weer enerzijds de nodes (=keys) te vinden zijn die deze file bezitten en een intger value (=value) die aangeeft of het om replicated, local of downloaded gaat.
                    entry.getKey().setFileOwner(true);
                    ClientComm.replicate(entry.getKey(), nodeData.getPrevNode());
                    entry.getKey().getFileLog().remove(noteData);               // voor hij shutdowned moet hij zich verwijderen als bezitter van de file in de log van de file
                    //nodeData.getfileLog().remove(entry);                      //verwijder fileLog van deze file uit lijst van fileLog want hij is niet meer owner.
                    //gaat die for lus nog correct runnen als ik binnen de lus een entry verwijder?
                }
            }
    }




}
