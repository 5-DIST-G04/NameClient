package com.distributed.ta;

import com.distributed.common.FileData;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("Files")
public class FileAPI {
    private NodeData nodeData = NodeData.getInstance();


    @POST
    @Consumes()
    public void receiveFile(FileData fileData) {
        nodeData.getFileManager().receiveNewFile(fileData);
    }
}
