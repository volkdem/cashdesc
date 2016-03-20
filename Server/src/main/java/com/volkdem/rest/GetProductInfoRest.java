package com.volkdem.rest;

import com.common.model.Product;
import com.common.model.Store;
import com.volkdem.storage.StorageItems;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by Vadim on 19.02.2016.
 */
@Path("/product")
public class GetProductInfoRest {



    @GET
    @Produces("application/json")
    public Response getProduct(@QueryParam("storeId") String storeId,
                              @QueryParam("productBarcode") String productBarcode) {

        System.out.println("Store id " + storeId);
        System.out.println("Product code " + productBarcode);

        Long storeidL = new Long(storeId);
        boolean isStoreExist = StorageItems.getStorageStore().containsKey(storeidL);
        System.out.println("Store is exist: " + isStoreExist);

        if(!isStoreExist) {
            String out = "Store not found with id: " + storeId;
            return Response.status(500).entity(out).build();
        } else {

            Store store = StorageItems.getStorageStore().get(storeidL);
            System.out.println("Store found: " + store.toString());

            for(Product product: store.getProductIdList()) {
                System.out.println("Product bar code: " + product.getBarcode());
                BigInteger requestBarcode = new BigInteger(productBarcode);
                if(requestBarcode.equals(product.getBarcode())) {
                    System.out.println("Found in store product " + product.toString());

                    ObjectMapper mapper = new ObjectMapper();
                    String jsonInString = null;
                    try {
                        jsonInString = mapper.writeValueAsString(product);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return Response.ok(jsonInString, MediaType.APPLICATION_JSON).build();
                }
            }
        }
        String ifNothingHappendEventially = "Could not process request with store id " + storeId + " product barcode " + productBarcode + ", maybe product barcode is wrong";
        return Response.status(500).entity(ifNothingHappendEventially).build();
    }



}
