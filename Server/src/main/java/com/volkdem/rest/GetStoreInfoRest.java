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
    public Response getStore(@QueryParam("store_barcode") String store_barcode) {

        String out = "Метод getStore() вызван. Ищем магазин по штрихкоду: " + store_barcode + "\n";

        Store store = StorageItems.getStorageStore().get(store_barcode);

        if(store == null) {
            out = "Не найден магазин по штрихкоду: " + store_barcode;
            Response.status(500).entity(out).build();
            try {
                throw new Exception("Store not found in HashMap storage with store barcode " + store_barcode);
            } catch (Exception e) {
                e.getMessage();
            }
        }

        out = "Адрес: " + out + store.getAddress();
        out = "Штрих код: " + out + store.getBarсode();
        out = "Название магазина: " + out + store.getName();
        out = out + store.getStore_ID();
        return Response.status(200).entity(out).build();
    }




}
