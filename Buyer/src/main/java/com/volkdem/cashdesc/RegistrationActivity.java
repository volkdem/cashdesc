package com.volkdem.cashdesc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Intent startScanShopActivity = new Intent( getApplicationContext(), ScanShopCodeActivity.class );
        startActivity(startScanShopActivity);
    }
}
