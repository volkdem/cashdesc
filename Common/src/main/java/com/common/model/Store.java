package com.common.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Vadim on 16.02.2016.
 */
public class Store {


    public Store() { super(); }

 
    private Long store_ID; //Store id
    private String name; //Store name
    private String address; //Store address
    private BigInteger barсode; //Store barcode

    /*
    Product ID.
    Made product_ID in Store class because it easy to fetch all products belong to Store
    */
    private List<Long> product_ID = new ArrayList<Long>();


    public Long getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(Long store_ID) {
        this.store_ID = store_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getBarсode() {
        return barсode;
    }

    public void setBarсode(BigInteger barсode) {
        this.barсode = barсode;
    }

    public List<Long> getProduct_ID() { return product_ID; }

    public void setProduct_ID(List<Long> product_ID) { this.product_ID = product_ID; }



}
