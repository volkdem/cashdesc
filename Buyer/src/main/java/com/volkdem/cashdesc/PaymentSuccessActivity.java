package com.volkdem.cashdesc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.common.model.Store;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.utils.StaticContainer;

public class PaymentSuccessActivity extends AppCompatActivity  {
    private static final String TAG = PaymentSuccessActivity.class.getSimpleName();
    private MainMenu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        toolbar.setNavigationIcon( R.drawable.ic_drawer );

        mainMenu = new MainMenu(this);

        String paymentCode = this.getIntent().getStringExtra( PaymentConfirmationActivity.PAYMENT_CODE );
        Log.d( TAG, "Payment code: " + paymentCode );

        TextView paymentCodeView = (TextView) findViewById( R.id.payment_code );
        paymentCodeView.setText( paymentCode );

        OrderWrapper order = StaticContainer.getOrder();
        Store store = order.getStore();

        String storeInfo =  getResources().getString( R.string.store_info, new Object[] { store.getName(), store.getAddress() } );
        TextView storeInfoView = (TextView) findViewById( R.id.store_info );
        storeInfoView.setText( storeInfo );

        ListView orderDetailView = (ListView) findViewById( R.id.order_details );
        orderDetailView.setAdapter( new ProductListAdapter( order, false ) );

        TextView sumView = (TextView) findViewById(R.id.paid);
        sumView.setText(order.getCost().toString());

        final Button newPurchase = (Button) findViewById( R.id.new_purchase );
        newPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNewPurchase();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK ) {
            goToNewPurchase();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goToNewPurchase() {
        Intent newPurchaseIntent = new Intent( PaymentSuccessActivity.this, ScanShopCodeActivity.class );
        newPurchaseIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( newPurchaseIntent );
        PaymentSuccessActivity.this.finish();
    }

}
