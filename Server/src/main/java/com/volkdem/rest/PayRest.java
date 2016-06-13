package com.volkdem.rest;

import com.common.jackson.ProductDeserializer;
import com.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volkdem.storage.PaymentCodePool;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Vadim on 19.02.2016.
 */
@Path("/pay")
public class PayRest {



    @POST
    @Consumes("application/json")
    public Response makePay(String orderJSON) {


        String jsonInString = null;
        Order theOrder = null;


        if(orderJSON == null) {
            try {
                throw new Exception("String JSON order is null and cannot be used...");

            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
            }
        }



        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ProductDeserializer());


        try {

            theOrder = mapper.readValue(orderJSON, Order.class);

            theOrder.setPaymentCode(new PaymentCodePool().getPayCodeFromPool());

            boolean isProductsNotExist = theOrder.getProducts().isEmpty();

            if(isProductsNotExist) {
                String out = "Nothing to pay for, no products found" + theOrder.getProducts().toString();
                return Response.status(500).entity(out).build();
            } else {
                jsonInString = mapper.writeValueAsString(theOrder);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok(jsonInString, MediaType.APPLICATION_JSON).build();
    }



}
