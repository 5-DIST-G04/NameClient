package com.distributed.ta;

import com.distributed.common.File;
import com.distributed.common.Node;

import java.io.*;
import java.util.*;
import java.util.Map;

public class NodeData {
    private static NodeData ourInstance = new NodeData();

    public static NodeData getInstance() {
        return ourInstance;
    }

    private Node thisNode;
    private Node nextNode;
    private Node prevNode;
    private String serverUri;

    private List<File> downloaded;
    private List<File> local;
    private List<File> replicated;
    private HashMap<File,HashMap<Node,Integer>> Fileslog;

    private NodeData() {

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




    public void addReplicated(File file) {
        replicated.add(file);
    }

    public void addLocal(File file) {
        local.add(file);
    }

    public void addDownloaded(File file) {
        downloaded.add(file);
    }

    public void addfileLog(File file) {
        Fileslog.put(file,file.getFileLog());
    }

    public HashMap<File,HashMap<Node,Integer>> getfileLog() {
        return Fileslog;
    }

}
