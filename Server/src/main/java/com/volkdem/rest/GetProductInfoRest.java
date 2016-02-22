package com.volkdem.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;

/**
 * Created by Vadim on 19.02.2016.
 */
@Path("/product")
public class GetProductInfoRest {



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getProduct(@QueryParam("product_barcode") String product_barcode) {
        String out = "You successfully called method getProduct() with barcode " + product_barcode;
        return Response.status(200).entity(out).build();
    }



}
