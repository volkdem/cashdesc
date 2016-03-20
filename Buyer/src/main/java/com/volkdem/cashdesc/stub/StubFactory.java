package com.volkdem.cashdesc.stub;

import com.common.model.Product;
import com.common.model.Store;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Evgeny on 16.03.2016.
 */
public class StubFactory {
    private final static Store unknownStore;
    private final static Product unknownProduct;


    private final static Map<String, Store > stores = new HashMap<>();
    private final static Map<String, Product > products = new HashMap<>();

    static {
        // ------ store
        unknownStore = new Store();
        unknownStore.setName( "Unkown Store");
        unknownStore.setAddress( "Unknown Address");
        unknownStore.setStoreId( -1L );
        unknownStore.setBarcode( new BigInteger( "0" ) );
        // ---- product
        unknownProduct = new Product();
        unknownProduct.setBarcode( new BigInteger( "0" ) );
        unknownProduct.setPrice( new BigDecimal( "0.0") );
        unknownProduct.setProductId( 0L );
        unknownProduct.setProductName( "Unknown Product" );


    }

    public static Store getStore( String barCode ) {
        Store store = new Store();
        store.setBarcode( new BigInteger( barCode ) );
        store.setName("Store " + barCode);
        store.setAddress("Address " + barCode);

        return store;
        //return unknownStore;
    }

    public static Product getProduct( Long storeId, String barCode ) {
        Product product = new Product();
        product.setProductName( "Product name " + barCode);
        product.setProductId( Long.valueOf( barCode ));
        product.setBarcode( new BigInteger( barCode ) );
        product.setPrice( new BigDecimal( barCode ).setScale( 2 ) );

        return product;
        //return unknownProduct;
    }

}

