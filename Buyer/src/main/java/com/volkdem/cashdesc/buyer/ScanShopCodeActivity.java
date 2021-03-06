package com.volkdem.cashdesc.buyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.volkdem.cashdesc.R;
import com.volkdem.cashdesc.buyer.model.OrderWrapper;
import com.volkdem.cashdesc.buyer.ui.IUnlockListener;
import com.volkdem.cashdesc.buyer.ui.IViewFinder;
import com.volkdem.cashdesc.buyer.ui.SreenLocker;
import com.volkdem.cashdesc.buyer.utils.BaseJsonRequest;
import com.volkdem.cashdesc.buyer.utils.Const;
import com.volkdem.cashdesc.buyer.utils.StaticContainer;

import java.util.Collection;


public class ScanShopCodeActivity extends ScanCodeActivity implements SurfaceHolder.Callback, IViewFinder, IUnlockListener {
    private static final String TAG = Const.TAG + ScanShopCodeActivity.class.getSimpleName();

    private SreenLocker screenLocker;
    private RequestQueue requestQueue;
    private MainMenu mainMenu;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon( R.drawable.ic_drawer );


        screenLocker = new SreenLocker(this);
        requestQueue = Volley.newRequestQueue(this);


        mainMenu = new MainMenu( this );


    }

    @Override
    protected Collection<BarcodeFormat> getBarcodeFormats() {
        return DecodeFormatManager.PRODUCT_FORMATS;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_shop_code;
    }

    @Override
    protected void onDecode(final Result rawResult, Bitmap barcode, float scaleFactor) {


        /*
         * DEBUG ROTATION
        ImageView image = (ImageView) findViewById(R.id.scanned_image );
        image.setImageBitmap( barcode );

        if( rawResult == null ) {
            restartPreviewAfterDelay(0L);
            return;
        }
         */

        Log.d(TAG, "scanned code is " + rawResult.getText());


        final String url = getStoreUrl(rawResult.getText());
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

                        restartPreviewAfterDelay(0L);


                    }
                });


        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
        screenLocker.lockScreen();
    }


    private void createOrder(Store store) {
        OrderWrapper order = new OrderWrapper(new Order());
        order.setStore(store);
        StaticContainer.setOrder(order);
    }

    private void goToScanProduct() {
        Intent scanProductActivityIntent = new Intent(ScanShopCodeActivity.this, ScanProdcutActivity.class);
        startActivity(scanProductActivityIntent);
    }

    private String getStoreUrl(String barCode) {
        return Const.URL + "store?storeBarcode=" + barCode;
    }

    @Override
    public void onUnlock() {
        requestQueue.cancelAll(TAG);
    }

}
