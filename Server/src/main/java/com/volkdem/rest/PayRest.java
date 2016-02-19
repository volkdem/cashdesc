package com.volkdem.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Vadim on 19.02.2016.
 */
@Path("/pay")
public class PayRest {



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response makePay() {
        return Response.status(200).build();
    }



}
