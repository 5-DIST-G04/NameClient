package com.practicum;

public class Node {
    private String ipAddress;
    private String name;

    public Node(){ }

    public Node(String name, String ip){
        this.ipAddress = ip;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getHash(){
        return Math.abs(this.name.hashCode()) % 32768;
    }
}
