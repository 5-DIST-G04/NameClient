package com.distributed.ta;


import com.distributed.common.MulticastPublisher;
import com.distributed.common.MulticastReceiver;
import com.distributed.common.ServerComm;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.distributed.common.Node;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URI;
import java.net.UnknownHostException;


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

    }

    public void Stop(){
        receive.stop();
        stopServer();
    }

    public void ShutDown(){
        ServerComm.RemoveOwnNode();
        this.Stop();
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
        server.shutdownNow();
    }



}
