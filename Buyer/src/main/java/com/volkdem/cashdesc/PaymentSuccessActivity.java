package com.volkdem.cashdesc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.common.model.Store;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.math.BigDecimal;
import java.util.Observer;

public class PaymentSuccessActivity extends AppCompatActivity  {
    private static final String TAG = PaymentSuccessActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);



        String paymentCode = this.getIntent().getStringExtra( PaymentConfirmationActivity.PAYMENT_CODE );
        Log.d( TAG, "Payment code: " + paymentCode );

        TextView paymentCodeView = (TextView) findViewById( R.id.payment_code );
        paymentCodeView.setText( paymentCode );

        OrderWrapper order = StaticContainer.getOrder();
        Store store = order.getStore();
        // TODO get data from string.xml
        String storeInfo =  getResources().getString( R.string.store_info, new Object[] { store.getName(), store.getAddress() } );
        TextView storeInfoView = (TextView) findViewById( R.id.store_info );
        storeInfoView.setText( storeInfo );

        ListView orderDetailView = (ListView) findViewById( R.id.order_details );
        orderDetailView.setAdapter( new ProductListAdapter( order, false ) );

        TextView sumView = (TextView) findViewById(R.id.sum);
        sumView.setText(order.getCost().toString());

    }

}
