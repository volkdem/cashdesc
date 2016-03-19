package com.volkdem.cashdesc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.common.model.Product;
import com.common.model.Store;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        ListView prodcutListView = (ListView)findViewById( R.id.product_list );
        prodcutListView.setAdapter( new ProductListAdapter( StaticContainer.getOrder() ) );

    }



}
