package com.volkdem.cashdesc.model;

import com.common.model.Order;
import com.common.model.Product;
import com.common.model.Store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Evgeny on 19.03.2016.
 */
public class OrderWrapper extends Observable {
    private Order order;
    private ArrayList< Product > productOrder = new ArrayList<>();

    public OrderWrapper(Order order) {
        this.order = order;
    }


    public String getId() {
        return order.getId();
    }

    public void setId( String id ) {
        this.order.setId( id );
    }


    public void addProduct(Product product) {
        order.addProduct( product );
        productOrder.remove( product);
        productOrder.add( 0, product );
        setChanged();
        notifyObservers();
    }

    public BigDecimal getCost() {
        return order.getCost();
    }

    public boolean containsProduct( Product product ) {
        return  order.containsProduct( product );
    }

    public void removeProduct(Product product) {
        order.removeProduct( product );
        productOrder.remove( product );
        setChanged();
        notifyObservers();
    }


    public void clear() {
        order.clear();
        setChanged();
        notifyObservers();
    }



    public Map<Product, Integer> getProducts() {
        return order.getProducts();
    }

    @Override
    public String toString() {
        return order.toString();
    }

    public boolean isEmpty() {
        return order.isEmpty();
    }

    public Store getStore() {
        return order.getStore();
    }

    public void setStore(Store store) {
        order.setStore( store );
    }

    public int getTotalSize() {
        return order.getTotalSize();
    }

    public int getProductTypeSize() {
        return order.getProductTypeSize();
    }

    public Product getItem(int position) {
        return productOrder.get( position );
    }

    public long getItemId(int position) {
        return productOrder.get( position ).getProductId();
    }

    public int getCount(Product product) {
        return order.getCount( product );
    }
}
