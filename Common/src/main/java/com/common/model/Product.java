package com.common.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Vadim on 16.02.2016.
 */
public class Product {



    public Product() { super(); }




    private Long product_id; //product ID
    private String productName; //product name
    private BigDecimal price; //price
    private BigInteger barcode; //bar code
    private Long store_id; //store id



    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigInteger getBarcode() {
        return barcode;
    }

    public void setBarcode(BigInteger barcode) {
        this.barcode = barcode;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }


}
