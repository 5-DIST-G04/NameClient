package com.practicum;

public class StatusExeption extends Exception {
    private int statuscode;

    public StatusExeption(int statuscode){
        this.statuscode = statuscode;
    }

    public int getStatuscode() {
        return statuscode;
    }
}
