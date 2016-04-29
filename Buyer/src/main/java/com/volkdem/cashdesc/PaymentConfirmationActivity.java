package com.volkdem.cashdesc;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.common.model.Order;
import com.common.model.Product;
import com.common.model.Store;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class PaymentConfirmationActivity extends AppCompatActivity implements Observer {
    private static final String TAG = Const.TAG + PaymentConfirmationActivity.class.getSimpleName();
    public static final String PAYMENT_CODE = PaymentConfirmationActivity.class.getName() +  "PAYMENT_CODE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setTitle( R.string.my_cart );


        ListView prodcutListView = (ListView) findViewById(R.id.product_list);
        prodcutListView.setAdapter(new ProductListAdapter(StaticContainer.getOrder(), true));

        OrderWrapper order = StaticContainer.getOrder();
        updateSum( order.getCost() );

        order.addObserver( this );

        Button payButton = (Button) findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue paymentRequestQueue = Volley.newRequestQueue(PaymentConfirmationActivity.this);
                StringRequest paymentRequest = new StringRequest(Request.Method.GET, Const.URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String paymentCode) {
                                goToSuccessPaymentActivity( paymentCode );
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO take the message from the string.xml
                                goToSuccessPaymentActivity( "34353453");
                                Toast.makeText(PaymentConfirmationActivity.this, "Invalid response " + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

                paymentRequestQueue.add( paymentRequest );
            }
        });


    }

    private void goToSuccessPaymentActivity( String paymentCode ) {
        Intent goToSusscessPayment = new Intent( PaymentConfirmationActivity.this, PaymentSuccessActivity.class );
        // TODO get from the response
        paymentCode = "64964968";
        goToSusscessPayment.putExtra( PAYMENT_CODE, paymentCode );
        goToSusscessPayment.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( goToSusscessPayment );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.home: {
                NavUtils.navigateUpFromSameTask( this );
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSum(BigDecimal sum) {
        TextView sumView = (TextView) findViewById(R.id.sum);
        sumView.setText(sum.toString());

    }

    @Override
    public void update(Observable observable, Object data) {
        OrderWrapper order = (OrderWrapper) observable;
        updateSum( order.getCost() );

        Button payButton = (Button) findViewById(R.id.pay_button);
        payButton.setEnabled( order.getTotalSize() > 0 );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OrderWrapper order = StaticContainer.getOrder();
        order.deleteObserver( this );
    }
}
