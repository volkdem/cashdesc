package com.volkdem.rest;

import com.common.model.Store;
import com.volkdem.storage.StorageItems;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * Created by Vadim on 18.02.2016.
 */
@Path("/store")
public class GetStoreInfoRest {


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStore(@QueryParam("store_barcode") String store_barcode) {

        String out = "Метод getStore() вызван. \n Ищем магазин по штрихкоду: " + store_barcode + "\n";


        if(!StorageItems.getStorageStore().containsKey(store_barcode)) {
            out = "Не найден магазин по штрихкоду: " + store_barcode;
            Response.status(500).entity(out).build();
            try {
                throw new Exception("Store not found in HashMap storage with store barcode " + store_barcode);
            } catch (Exception e) {
                e.getMessage();
            }
        }


        Store store = StorageItems.getStorageStore().get(store_barcode);



        out = out + " Найден магазин \n";
        out = out + " Адрес: " + store.getAddress() + "\n";
        out = out + " Штрих код: " + store.getBarсode() + "\n";
        out = out + " Название магазина: " + store.getName() + "\n";
        out = out + " ID магазина: " + store.getStore_ID() + "\n";

        String outWithUTF = null;
        try { outWithUTF = new String(out.getBytes(), "UTF-8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }

        return Response.status(Response.Status.OK).entity(outWithUTF).header("charset", "utf-8").build();
    }




}
