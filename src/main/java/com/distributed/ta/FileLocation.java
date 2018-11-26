package com.distributed.ta;

public class FileLocation {
    private String fileName, ipAddress;

    public FileLocation(){}

    public FileLocation(String fileName, String ipAddress){
        this.fileName = fileName;
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
