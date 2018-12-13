package com.distributed.ta;

import com.distributed.common.FileData;
import com.distributed.common.Node;

import java.util.*;

public class NodeData {
    private static NodeData ourInstance = new NodeData();

    public static NodeData getInstance() {
        return ourInstance;
    }

    private Node thisNode;
    private Node nextNode;
    private Node prevNode;
    private String serverUri;
    private FileManager fileManager;



    private NodeData() {

    }

    public void Init(){
        fileManager = new FileManager();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public String getServerUri() {
        return serverUri;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    public Node getThisNode() {
        return thisNode;
    }

    public void setThisNode(Node thisNode) {
        this.thisNode = thisNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Node getPrevNode() {
        return prevNode;
    }

    public void setPrevNode(Node prevNode) {
        this.prevNode = prevNode;
    }

    public String getBaseUri(){
        return "http://" + this.thisNode.getIpAddress() + ":8080/";
    }


}
