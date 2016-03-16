package com.volkdem.cashdesc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.common.model.Order;
import com.google.zxing.Result;
import com.volkdem.cashdesc.stub.StubFactory;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.nio.charset.Charset;

public class ScanProdcutActivity extends ScanCodeActivity {
    private static final String TAG = ScanProdcutActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_prodcut;
    }

    @Override
    protected void onDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.d( TAG, "onDecode" );
        Order order = StaticContainer.getOrder();

        order.addProduct( StubFactory.getProduct( order.getStore().getStore_ID(), rawResult.getText() ) );

        Log.i( TAG, "orderSize=" + order.getSize() );

        if( order.getSize() == Const.MAX_ORDER_SIZE ) {
            Intent paymentConfirmationActivityIntent = new Intent(this, PaymentConfirmationActivity.class);
            startActivity(paymentConfirmationActivityIntent);
        }
    }
}
