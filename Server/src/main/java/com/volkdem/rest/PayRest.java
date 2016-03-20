package com.volkdem.rest;

import com.common.model.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Vadim on 19.02.2016.
 */
@Path("/pay")
public class PayRest {



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makePay(Order order) {

        System.out.println("Hello from pay");
        System.out.println(order.toString());



        String out = "Method makePay() \n " +
                "make pay with order id: " + order.getId() + ", \n " +
                "store " + order.getStore() + ", \n" +
                "products " + order.getProducts();
        return Response.status(Response.Status.OK).entity(out + "\n Payment is successful --> STUB").header("charset", "utf-8").build();

    }



}
