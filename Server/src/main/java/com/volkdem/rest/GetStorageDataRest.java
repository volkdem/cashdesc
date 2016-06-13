package com.volkdem.rest;

import com.common.model.Product;
import com.common.model.Store;
import com.volkdem.storage.StorageItems;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Vadim on 11.06.2016.
 */
@Path("/storage")
public class GetStorageDataRest {


        @GET
        @Produces("application/json")
        public Response getStorageData() {

            String jsonResponce = "";
            ObjectMapper objectMapper = new ObjectMapper();

            if(StorageItems.getStorageStore().size() <= 0
               || StorageItems.getStorageStore() == null) {
                return Response.status(500).entity("[ERROR] Store size is: " + StorageItems.getStorageStore().size()).build();
            }


            for(Map.Entry<Long, Store> entry :  StorageItems.getStorageStore().entrySet()) {

                try {
                    Long key = entry.getKey();
                    Store store =  entry.getValue();
                        jsonResponce = jsonResponce + objectMapper.writeValueAsString(store) + "\n";
                        /*for(Product product : store.getProductIdList()) {
                                 jsonResponce = objectMapper.writeValueAsString(product);
                         }*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return Response.ok().entity(jsonResponce).build();
        }


}
