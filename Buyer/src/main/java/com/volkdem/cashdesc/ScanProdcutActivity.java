package com.volkdem.cashdesc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.model.Order;
import com.common.model.Product;
import com.google.zxing.Result;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.stub.StubFactory;
import com.volkdem.cashdesc.utils.BaseJsonRequest;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.nio.charset.Charset;
import java.text.MessageFormat;

public class ScanProdcutActivity extends ScanCodeActivity {
    private static final String TAG = Const.TAG + ScanProdcutActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_prodcut;
    }

    @Override
    protected void onDecode(final Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.d( TAG, "onDecode(). Scanned barcode=" + rawResult.getText() );

        final OrderWrapper order = StaticContainer.getOrder();

        RequestQueue requestQueue = Volley.newRequestQueue( this );
        String url = getProductUrl( order.getStore().getStoreId(), rawResult.getText() );

        BaseJsonRequest<Product> productRequest = new BaseJsonRequest<Product>( Product.class, Request.Method.GET, url, null,
                new Response.Listener<Product>() {

                    @Override
                    public void onResponse(Product product) {
                        order.addProduct( product );
                        Log.d( TAG, "Found prodcut: " + product.getProductName() );

                        Log.i( TAG, "orderSize=" + order.getTotalSize() );

                        if( order.getTotalSize() == Const.MAX_ORDER_SIZE ) {
                            Intent paymentConfirmationActivityIntent = new Intent(ScanProdcutActivity.this, PaymentConfirmationActivity.class);
                            startActivity(paymentConfirmationActivityIntent);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO user message from the strings.xml
                        Toast.makeText(ScanProdcutActivity.this, "Error on getting product + " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add( productRequest );



    }

    private String getProductUrl(Long storeId, String productBarCode ) {
        return MessageFormat.format("{0}product?storeId={1}&productBarcode={2}", Const.URL, storeId, productBarCode );
   }
}
