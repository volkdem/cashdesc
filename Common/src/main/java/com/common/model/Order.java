package com.common.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Evgeny on 05.12.2015.
 */
public class Order implements Serializable {


    private Long id;
    private Integer paymentCode;
    private Store store;
    private Date paymentDate;
    private boolean checkStatus;    // Define if officer has checked this order

    public void setProducts(Map<Product, Integer> products) {
        this.products = products;
    }

    private Map<Product, Integer> products = new HashMap<Product, Integer>();


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(Integer paymentCode) {
        this.paymentCode = paymentCode;
    }

    public void addProduct(Product product) {
        Integer count = products.get( product );
        if ( count == null )
            count = 0;

        count++;

        products.put( product, count );
    }

    @JsonIgnore
    public BigDecimal getCost() {
        BigDecimal cost = new BigDecimal( 0 ).setScale( 2 );
        for( Product product: products.keySet() ) {
            cost = cost.add( product.getPrice().multiply( new BigDecimal( products.get( product ) ) ) );
        }

        return cost;
    }

    @JsonIgnore
    public boolean containsProduct( Product product ) {
        return  products.containsKey( product );
    }


    public void removeProduct(Product product) {
        products.remove( product );

    }


    public void clear() {
        products.clear();
    }



    public Map<Product, Integer> getProducts() {
        return products;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", paymentCode=" + paymentCode +
                ", store=" + store +
                ", paymentDate=" + paymentDate +
                ", checkStatus=" + checkStatus +
                ", products=" + products +
                '}';
    }


    @JsonIgnore
    public boolean isEmpty() {
        return getProducts().size() == 0;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @JsonIgnore
    public int getTotalSize() {
        int size = 0;
        for( Integer count: products.values() ) {
            size += count;
        }

        return size;
    }

    @JsonIgnore
    public int getProductTypeSize() {
        return products.size();
    }

    @JsonIgnore
    public int getCount(Product product) {
        Integer count = products.get( product );
        if( count == null ) {
            return 0;
        }

        return count;
    }

    public boolean isCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    @JsonIgnore
    public void setCheckStatus(int checkStatus) {
        this.checkStatus = ( checkStatus == 0 ) ? false : true;
    }


}
