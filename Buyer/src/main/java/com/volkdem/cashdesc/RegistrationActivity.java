package com.volkdem.cashdesc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.common.model.Order;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.stub.StubFactory;
import com.volkdem.cashdesc.utils.StaticContainer;

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


        Intent startScanShopActivity = new Intent( getApplicationContext(), ScanShopCodeActivity.class );
        startScanShopActivity.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(startScanShopActivity);
        finish();

    }
}
