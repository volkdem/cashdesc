package com.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Created by Vadim on 16.02.2016.
 */
public class Product implements Serializable {



    public Product() { super();
    }



    private Long productId;
    private String productName;
    private BigDecimal price;
    private BigInteger barcode;
    /*
    Product ID.
    Made remove store_id from Product class because it harder to get products belong to particular Store
    */



    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // TODO move scaling to another place
    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_UP);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (productId != null ? !productId.equals(product.productId) : product.productId != null) return false;
        if (productName != null ? !productName.equals(product.productName) : product.productName != null) return false;
        if (price != null ? !price.equals(product.price) : product.price != null) return false;
        return !(barcode != null ? !barcode.equals(product.barcode) : product.barcode != null);

    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (barcode != null ? barcode.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", barcode=" + barcode +
                '}';
    }

}

