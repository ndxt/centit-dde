package com.centit.dde.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/httpTask")
public interface RestFulPacketWebServices {
    //@Consumes(MediaType.APPLICATION_JSON)		   //接收json的数据
    //@Produces(MediaType.APPLICATION_JSON)          //返回json格式的数据

    @GET
    @Path("/runTest/{packetId}")
    @Produces(MediaType.WILDCARD)
    void runTestTaskExchange(@PathParam("packetId") String packetId, @Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException;

    @GET
    @Path("/debugRunTest/{packetId}")
    void debugRunTestTaskExchange(@PathParam("packetId")String packetId,@Context HttpServletRequest request, @Context HttpServletResponse response)throws IOException;

    @GET
    @Path("/run/{packetId}")
    void runTaskExchange(@PathParam("packetId") String packetId,@Context HttpServletRequest request, @Context HttpServletResponse response)throws IOException;

    @GET
    @Path("/debugRun/{packetId}")
    void debugRunTaskExchange(@PathParam("packetId")String packetId,@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException;

    @POST
    @Path("/runPostTest/{packetId}")
    void runPostTest(@PathParam("packetId")String packetId,@Context HttpServletRequest request, @Context HttpServletResponse response)throws IOException;


    @POST
    @Path("/runPost/{packetId}")
    void runTaskPost(@PathParam("packetId")String packetId,@Context HttpServletRequest request, @Context HttpServletResponse response)throws IOException;
}
