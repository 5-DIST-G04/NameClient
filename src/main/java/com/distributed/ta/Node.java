package com.distributed.ta;

public class Node {
    private String ipAddress;
    private String name;
    private int nextHash;
    private int previousHash;
    private String nextIP;
    private String previousIP;

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

    public void setNextHash(int next) {
        this.nextHash = next;
    }

    public void setPreviousHash(int previous) {
        this.previousHash = previous;
    }

    public int getNextHash(){return this.nextHash; }

    public int getPreviousHash(){return this.previousHash; }

    public void setNextIP(String ip) {
        this.nextIP = ip;
    }

    public void setPreviousIP(String ip) {
        this.previousIP = ip;
    }

    public String getNextIP(){return this.nextIP; }

    public String getPreviousIP(){return this.previousIP; }
}
