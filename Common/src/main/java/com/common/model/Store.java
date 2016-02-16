package com.common.model;

import java.math.BigInteger;

/**
 * Created by Vadim on 16.02.2016.
 */
public class Store {


    public Store() { super(); }

 
    private Long store_ID; //ID магазина
    private String name; //Имя магазина
    private String address; //Адрес магазина
    private BigInteger barсode; //Штрих код


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


}
