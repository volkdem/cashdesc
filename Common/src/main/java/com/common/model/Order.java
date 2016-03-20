package com.common.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Evgeny on 05.12.2015.
 */
public class Order implements Serializable {
    private String id;
    private Store store;
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }


    public void addProduct(Product product) {
        Integer count = products.get( product );
        if ( count == null ) {
            count = 0;
        }

        count++;

        products.put( product, count );
    }

    public BigDecimal getCost() {
        BigDecimal cost = new BigDecimal( 0 ).setScale( 2 );
        for( Product product: products.keySet() ) {
            cost = cost.add( product.getPrice().multiply( new BigDecimal( products.get( product ) ) ) );
        }

        return cost;
    }

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
                "orderID='" + id + '\'' +
                ", itemsList=" + products +
                '}';
    }

    public boolean isEmpty() {
        return getProducts().size() == 0;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getTotalSize() {
        int size = 0;
        for( Integer count: products.values() ) {
            size += count;
        }

        return size;
    }

    public int getProductTypeSize() {
        return products.size();
    }

    public int getCount(Product product) {
        Integer count = products.get( product );
        if( count == null ) {
            return 0;
        }

        return count;
    }
}
