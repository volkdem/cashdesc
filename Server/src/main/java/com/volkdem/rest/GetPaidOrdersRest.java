package com.volkdem.rest;

import com.common.jackson.ProductDeserializer;
import com.common.model.Order;
import com.common.model.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import com.volkdem.storage.StorageItems;
import com.volkdem.storage.TimeTreeMapComparator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by Vadim on 02.07.2016.
 */
@Path("/paidOrders")
public class GetPaidOrdersRest {



    @GET
    @Path("amount")
    @Produces("application/json")
    public Response getProductsByCount(@QueryParam("ordersAmount") Integer returnAmount) {

        String jsonValue = null;
        TreeMap<java.sql.Timestamp, Order> paidOrdersAmount = new TreeMap<java.sql.Timestamp, Order>(new TimeTreeMapComparator());

        for(Map.Entry<Timestamp, Order> entry : StorageItems.getPaidOrders().entrySet()) {
           if(returnAmount !=0) {
              paidOrdersAmount.put(entry.getKey(), entry.getValue());
              returnAmount = returnAmount - 1;
               continue;
           } else {
               break;
           }
        }

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new ProductDeserializer());

            jsonValue = mapper.writeValueAsString(paidOrdersAmount);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().entity("Возвращаем " + returnAmount + "последний заказа \n" + jsonValue).build();
    }




    @GET
    @Path("newAfterId")
    @Produces("application/json")
    public Response getProductsGreaterID(@QueryParam("id") Long id) {

        String jsonValue = null;
        TreeMap<java.sql.Timestamp, Order> paidOrdersGreaterID = new TreeMap<java.sql.Timestamp, Order>(new TimeTreeMapComparator());

        try {

            for(Map.Entry<Timestamp, Order> entry : StorageItems.getPaidOrders().entrySet()) {
            if(entry.getValue().getId() > id) {
                paidOrdersGreaterID.put(entry.getKey(), entry.getValue());
                continue;
            } else {
                break;
            }
        }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new ProductDeserializer());

            jsonValue = mapper.writeValueAsString(paidOrdersGreaterID);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().entity("Найдено заказов: " + paidOrdersGreaterID.size() + " больше id: " + id + "\n" + jsonValue).build();
    }





}
