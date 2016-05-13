package com.volkdem.cashdesc.buyer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.common.model.Order;
import com.common.model.Product;
import com.common.model.Store;
import com.volkdem.cashdesc.R;
import com.volkdem.cashdesc.buyer.model.OrderWrapper;
import com.volkdem.cashdesc.buyer.utils.StaticContainer;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Just to test
        /*
        OrderWrapper order = new OrderWrapper( new Order() );
        StaticContainer.setOrder( order );
        order.setStore(StubFactory.getStore( "1"));
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "1") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "2") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "3300") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "2") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "4") );

        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "6") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "7") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "300") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "8") );
        /*order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "9") );


        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "10") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "20") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "33000") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "20") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "40") );

        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "11") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "21") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "31000") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "21") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "41") );

        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "12") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "22") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "32000") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "22") );
        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), "42") );


        Intent paymentConfirmationAct = new Intent( getApplicationContext(), PaymentConfirmationActivity.class );
        startActivity(paymentConfirmationAct);
    */

        OrderWrapper order = new OrderWrapper( new Order() );

        order.setId( "11");
        Store store = new Store();
        store.setAddress("Aviacionnay street, 55, ap.345");
        store.setBarcode( new BigInteger( "584561316574" ) );
        store.setName( "Пятёрочка");
        store.setStoreId( 1857L );


        order.setStore( store );

        Product product = new Product();
        product.setBarcode( new BigInteger( "564564" ) );
        product.setPrice( new BigDecimal(84.34));
        product.setProductId( 4654L );
        product.setProductName( "ZEWA: Бумажные одноразовые салфетки ");

        order.addProduct( product );
        order.addProduct( product );

        product = new Product();
        product.setBarcode( new BigInteger( "564523" ) );
        product.setPrice( new BigDecimal(198.33));
        product.setProductId( 46543L );
        product.setProductName( "Бородинский хлеб");
        order.addProduct( product );

        product = new Product();
        product.setBarcode( new BigInteger( "545564523" ) );
        product.setPrice( new BigDecimal(158.33));
        product.setProductId( 46542L );
        product.setProductName( "Торт \"Наполеон нежный\"");
        order.addProduct( product );

        StaticContainer.setOrder( order );

        Intent startScanShopActivity = new Intent( getApplicationContext(), PaymentConfirmationActivity.class );
        startScanShopActivity.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(startScanShopActivity);
        finish();

    }
}
