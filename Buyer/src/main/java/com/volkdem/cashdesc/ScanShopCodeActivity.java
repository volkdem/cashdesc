package com.volkdem.cashdesc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.model.Order;
import com.common.model.Store;
import com.google.zxing.Result;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.ui.IUnlockListener;
import com.volkdem.cashdesc.ui.IViewFinder;
import com.volkdem.cashdesc.ui.SreenLocker;
import com.volkdem.cashdesc.utils.BaseJsonRequest;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;


public class ScanShopCodeActivity extends ScanCodeActivity implements SurfaceHolder.Callback, IViewFinder, IUnlockListener {
    private static final String TAG = Const.TAG + ScanShopCodeActivity.class.getSimpleName();

    private SreenLocker screenLocker;
    private RequestQueue requestQueue;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        screenLocker = new SreenLocker( this );
        requestQueue = Volley.newRequestQueue( this );
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_shop_code;
    }

    @Override
    protected void onDecode(final Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.d( TAG, "scanned code is " + rawResult.getText() );


        final String url = getStoreUrl( rawResult.getText());
        // Const.URL;
        BaseJsonRequest<Store> stringRequest = new BaseJsonRequest<Store>(Store.class, Request.Method.GET, url, null,
                new Response.Listener<Store>() {

                    @Override
                    public void onResponse(Store store) {
                        Log.d(TAG, "Found store is " + store.toString());

                        createOrder(store);
                        screenLocker.unlockScreen();
                        goToScanProduct();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: show error message, check internet connection
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                        screenLocker.unlockScreen();
                        // TODO get message from string.xml
                        Toast.makeText(ScanShopCodeActivity.this, "Requst error + " + error.getMessage(), Toast.LENGTH_LONG).show();

                       /*
                       createOrder( StubFactory.getStore( rawResult.getText() ) );
                       goToScanProduct();
                       */


                    }
                });


        stringRequest.setTag( TAG );
        requestQueue.add( stringRequest );
        screenLocker.lockScreen();
    }

    private void createOrder(Store store) {
        OrderWrapper order = new OrderWrapper ( new Order() );
        order.setStore( store );
        StaticContainer.setOrder( order );
    }

    private void goToScanProduct() {
        Intent scanProductActivityIntent = new Intent(ScanShopCodeActivity.this, ScanProdcutActivity.class);
        startActivity(scanProductActivityIntent);
    }

    private String getStoreUrl( String barCode ) {
        return Const.URL + "store?storeBarcode=" + barCode;
    }

    @Override
    public void onUnlock() {
        requestQueue.cancelAll( TAG );
    }
}
