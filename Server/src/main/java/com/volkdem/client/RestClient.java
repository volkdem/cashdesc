package com.volkdem.client;


import com.common.jackson.ProductDeserializer;
import com.common.model.Order;
import com.common.model.Product;
import com.common.model.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Vadim on 19.02.2016.
 */
public class RestClient {


    public static void main(String[] args) {


        //DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            //HttpHost target = new HttpHost("localhost", 8080, "http");



            //This is server url changed
            /*
            All requests begin within:
            localhost:8080/Server/rest/...
            For store info = localhost:8080/Server/rest/store?store_barcode=123456
            For product info = localhost:8080/Server/rest/product?product_barcode=123456
            For pay = localhost:8080/Server/rest/pay
            */
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new ProductDeserializer());
            Order obj = new Order();

            final Product milk = new Product();
            milk.setBarcode(new BigInteger("54321"));
            milk.setProductName("Milk");
            milk.setPrice(new BigDecimal(67.0));
            milk.setProductId(1L);

            Store magnit = new Store();
            magnit.setAddress("sh. Entuziastov, 66");
            magnit.setBarcode(new BigInteger("1234567"));
            magnit.setName("Magnit");
            magnit.setStoreId(1L);
            magnit.setProductIdList(new ArrayList<Product>() {{add(milk);}});
            obj.setStore(magnit);
            obj.setId(123L);

            obj.addProduct(milk);



            //Object to JSON in String
            String jsonInString = mapper.writeValueAsString(obj);


            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/Server/rest/pay");

            //String input = "{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";

            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, jsonInString);

           /* if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }*/

            System.out.println("Output from Server .... \n");
            String output = response.getEntity(String.class);
            System.out.println(output);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}

            /*HttpGet getRequest = new HttpGet("/rest/pay");

            System.out.println("executing request to " + getRequest);

            HttpResponse httpResponse = httpclient.execute(getRequest);
            HttpEntity entity = httpResponse.getEntity();

            System.out.println("----------------------------------------");
            System.out.println(httpResponse.getStatusLine());
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                System.out.println(headers[i]);
            }
            System.out.println("----------------------------------------");

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
*/
 //   }

//}

