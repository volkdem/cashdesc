package com.volkdem.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;

/**
 * Created by Vadim on 18.02.2016.
 */
@Path("/store")
public class GetStoreInfoRest {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStore(@PathParam("store_barcode")BigInteger barcode) {
        String out = "You successfully called method getStore() with barcode " + barcode;
        return Response.status(200).entity(out).build();
    }




}
