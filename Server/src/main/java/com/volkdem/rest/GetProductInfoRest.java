package com.volkdem.rest;

import com.common.model.Product;
import com.common.model.Store;
import com.volkdem.storage.StorageItems;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by Vadim on 19.02.2016.
 */
@Path("/product")
public class GetProductInfoRest {



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getProduct(@QueryParam("product_barcode") String product_barcode) {

        String out = "Метод getProduct() \n Ищем продукт по штрихкоду:  " + product_barcode + "\n";


        if(!StorageItems.getStorageProduct().containsKey(product_barcode)) {
            out = "Не найден магазин по штрихкоду: " + product_barcode;
            Response.status(500).entity(out).build();
            try {
                throw new Exception("Product not found in HashMap storage with store barcode " + product_barcode);
            } catch (Exception e) {
                e.getMessage();
            }
        }

        Product product = StorageItems.getStorageProduct().get(product_barcode);


        out = out + " Найден продукт \n";
        out = out + " Имя продукта: " + product.getProductName() + "\n";
        out = out + " Штрих код продукта: " + product.getBarcode() + "\n";
        out = out + " Получаем цену продукта: " + product.getPrice() + "\n";
        out = out + " ID продукта: " + product.getProduct_id() + "\n";

        String outWithUTF = null;
        try { outWithUTF = new String(out.getBytes(), "UTF-8"); } catch (UnsupportedEncodingException e) { e.printStackTrace(); }

        return Response.status(200).entity(outWithUTF).build();
    }



}
