package com.distributed.ta;

import com.distributed.common.Node;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("Neighbours")
public class NeighbourApi {
    private NodeData nodeData = NodeData.getInstance();

    @POST
    @Path("/nextNode")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setNextNode(Node node){
        nodeData.setNextNode(node);
        return Response.status(204).build();
    }

    @POST
    @Path("/prevNode")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setPrevNode(Node node){
        nodeData.setPrevNode(node);
        return Response.status(204).build();
    }

}
