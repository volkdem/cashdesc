package com.volkdem.client;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Vadim on 19.02.2016.
 */
public class RestClient {


    public static void main(String[] args) {


        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // specify the host, protocol, and port
            HttpHost target = new HttpHost("localhost", 8080, "http");



            //This is server url changed
            /*
            All requests begin within:
            localhost:8080/Server/rest/...
            For store info = localhost:8080/Server/rest/store?store_barcode=123456
            For product info = localhost:8080/Server/rest/product?product_barcode=123456
            For pay = localhost:8080/Server/rest/pay
            */


            HttpGet getRequest = new HttpGet("/server.cashdesk/rest/store?store_barcode=123456789");

            System.out.println("executing request to " + target);

            HttpResponse httpResponse = httpclient.execute(target, getRequest);
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

    }

}

