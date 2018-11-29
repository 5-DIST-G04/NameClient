package com.distributed.ta;

import com.distributed.common.Node;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ServerData")
public class ClientApi {
    private NodeData nodeData = NodeData.getInstance();

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setNextNode(String serverUri){
        nodeData.setServerUri(serverUri);
        return Response.status(204).build();
    }
}
