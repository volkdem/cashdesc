package com.volkdem.cashdesc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.common.model.Order;
import com.common.model.Store;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.volkdem.cashdesc.camera.CameraManager;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.stub.StubFactory;
import com.volkdem.cashdesc.utils.BaseJsonRequest;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;

import org.json.JSONObject;

import java.nio.charset.Charset;


public class ScanShopCodeActivity extends ScanCodeActivity implements SurfaceHolder.Callback {
    private static final String TAG = Const.TAG + ScanShopCodeActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_shop_code;
    }

    @Override
    protected void onDecode(final Result rawResult, Bitmap barcode, float scaleFactor) {
        RequestQueue requestQueue = Volley.newRequestQueue( this );
        Log.d( TAG, "scanned code is " + rawResult.getText() );


        final String url = getStoreUrl( rawResult.getText());
        // Const.URL;
        BaseJsonRequest<Store> stringRequest = new BaseJsonRequest<Store>(Store.class, Request.Method.GET, url, null,
                new Response.Listener<Store>() {

                    @Override
                    public void onResponse(Store store) {
                        Log.d(TAG, "Found store is " + store.toString());

                        createOrder(store);
                        goToScanProduct();
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: show error message, check internet connection
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());

                        // TODO get message from string.xml
                        Toast.makeText(ScanShopCodeActivity.this, "Requst error + " + error.getMessage(), Toast.LENGTH_LONG).show();

                       /*
                       createOrder( StubFactory.getStore( rawResult.getText() ) );
                       goToScanProduct();
                       */


                    }
                });



        requestQueue.add( stringRequest );
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
}
