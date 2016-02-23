package com.common.model;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Vadim on 16.02.2016.
 */
public class Product {



    public Product() { super(); }



    private Long product_id; //ID продукта
    private String productName; //Имя продукта
    private BigDecimal price; //Цена
    private BigInteger barcode; //Штрих код
    /*
    Product ID.
    Made remove store_id from Product class because it harder to get products belong to particular Store
    */



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

}
