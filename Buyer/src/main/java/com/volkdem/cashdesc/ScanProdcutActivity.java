package com.volkdem.cashdesc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.zxing.Result;

public class ScanProdcutActivity extends ScanCodeActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_prodcut;
    }

    @Override
    protected void onDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        Intent paymentConfirmationActivityIntent = new Intent(this, PaymentConfirmationActivity.class);
        startActivity(paymentConfirmationActivityIntent);
    }
}
