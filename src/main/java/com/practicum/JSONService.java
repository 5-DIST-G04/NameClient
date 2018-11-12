package com.practicum;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;

public class JSONService {

    String serverURL = "http://localhost:8080/";
    Client client = ClientBuilder.newClient();
    WebTarget webTarget = client.target(serverURL);
    Invocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON);


    /*
    public getData(){

        Class class = builder.get(<choseclass>.class);


    }
    */

    public FileLocation getFileLocation(String file) {

        FileLocation fileLocation = new FileLocation(file,"");
        fileLocation = builder.post(Entity.entity(fileLocation,MediaType.APPLICATION_JSON),FileLocation.class);
        return fileLocation;
    }

}




