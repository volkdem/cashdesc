package com.volkdem.rest;

import com.common.model.Store;
import com.volkdem.storage.StorageItems;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Map;

/**
 * Created by Vadim on 18.02.2016.
 */
@Path("/store")
public class GetStoreInfoRest {



    @GET
    @Produces("application/json")
    public Store getStore(@QueryParam("storeBarcode") String storeBarcode) {

        for(Map.Entry<Long, Store> entry : StorageItems.getStorageStore().entrySet()) {

            System.out.println(entry.getValue().toString());
            Store store = (Store)entry.getValue();
            if(store.getBarcode().equals(new BigInteger(storeBarcode))) {
                return store;
            }
        }

        String out = "Not found store by barcode: " + storeBarcode;
        Response.status(500).entity(out).build();
        try {
            throw new Exception("Store not found in HashMap storage with store barcode " + storeBarcode);
        } catch (Exception e) {
            e.getMessage();
        }

        return null;

    }


}
