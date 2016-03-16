package com.volkdem.cashdesc.stub;

import com.common.model.Product;
import com.common.model.Store;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Evgeny on 16.03.2016.
 */
public class StubFactory {
    private final static Store unknownStore;
    private final static Product unknownProduct;

    static {
        // ------ store
        unknownStore = new Store();
        unknownStore.setName( "Unkown Store");
        unknownStore.setAddress( "Unknown Address");
        unknownStore.setStore_ID( -1L );
        unknownStore.setBarcode( new BigInteger( "0" ) );
        // ---- product
        unknownProduct = new Product();
        unknownProduct.setBarcode( new BigInteger( "0" ) );
        unknownProduct.setPrice( new BigDecimal( "0.0") );
        unknownProduct.setProduct_id( 0L );
        unknownProduct.setProductName( "Unknown Product" );
        unknownProduct.setStore_id( unknownStore.getStore_ID() );
    }

    public static Store getStore( String barCode ) {
        return unknownStore;
    }

    public static Product getProduct( Long storeId, String barCode ) {
        return unknownProduct;
    }


}

