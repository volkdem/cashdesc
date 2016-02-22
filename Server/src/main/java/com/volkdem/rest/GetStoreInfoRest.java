package com.volkdem.rest;

import com.common.model.Store;
import com.volkdem.storage.StorageItems;

import javax.ws.rs.*;
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
    public Response getStore(@QueryParam("store_id") String store_id) {
        String out = "You successfully called method getStore() with barcode " + store_id;
        Store store = StorageItems.storageStore.get(store_id);
        out = out + store.getAddress();
        out = out + store.getBarсode();
        out = out + store.getName();
        out = out + store.getStore_ID();
        return Response.status(200).entity(out).build();
    }




}
