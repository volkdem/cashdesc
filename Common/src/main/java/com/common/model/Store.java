package com.common.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim on 16.02.2016.
 */
public class Store implements Serializable {


    public Store() { super(); }

 
    private Long storeId;
    private String name;
    private String address;
    private BigInteger barcode;

    /*
    Product ID.
    Made productIdList in Store class because it easy to fetch all products belong to Store
    */
    private List<Product> productIdList = new ArrayList<Product>();


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public BigInteger getBarcode() { return barcode; }

    public void setBarcode(BigInteger barcode) {this.barcode = barcode; }

    public List<Product> getProductIdList() { return productIdList; }

    public void setProductIdList(List<Product> productIdList) { this.productIdList = productIdList; }


    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", barcode=" + barcode +
                ", productIdList=" + productIdList +
                '}';
    }
}
