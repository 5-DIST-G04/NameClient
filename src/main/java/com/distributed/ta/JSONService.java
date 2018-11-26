package com.distributed.ta;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class JSONService {

    private String targetURL;
    private Client client = ClientBuilder.newClient();
    private WebTarget webTarget;
    //private Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);

    public JSONService(String serverUrl){

        this.targetURL = serverUrl;
        this.webTarget = client.target(targetURL);
    }


    public FileLocation getFileLocation(String fileName) throws StatusExeption {
        Response response = webTarget.path("FileLocation/"+fileName).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200){
            throw new StatusExeption(response.getStatus());
        }
        FileLocation fileLocation = response.readEntity(FileLocation.class);
        return fileLocation;
    }





    public void SubmitName(String name) throws StatusExeption{
        String ip = "";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("The host server could not be found on the specified address");
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Node node =  new Node(name,ip);
        //System.out.println(node.getName() + node.getIpAddress());
        Response r = webTarget.path("NodeName").request(MediaType.TEXT_PLAIN).post(Entity.entity(node,MediaType.APPLICATION_JSON),Response.class);
        System.out.println(r.readEntity(String.class));
        if(r.getStatus() != 201)
            throw new StatusExeption(r.getStatus());
        return;
    }

    public void RemoveName(String name) throws StatusExeption{
        Response r = webTarget.path("NodeName/" + name).request(MediaType.TEXT_PLAIN).delete();
        if (r.getStatus() != 204)
            throw new StatusExeption(r.getStatus());
        return;
    }





    //--------------- NEW:  get ip of next node and force them to update their previouse node to our previouse node ----------------------


    public NodeIP getNodeIP(String fileName) throws StatusExeption {
        Response response = webTarget.path("NodeIP/"+fileName).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() != 200){
            throw new StatusExeption(response.getStatus());
        }
        NodeIP nodeIP = response.readEntity(NodeIP.class);
        return nodeIP;
    }


    public void setPreviouseNode(String previouseNode) throws StatusExeption {
        Response response = webTarget.path("setPreviousetNode/"+previouseNode).request().get();
        if (response.getStatus() != 200){
            throw new StatusExeption(response.getStatus());
        }


    }





}




