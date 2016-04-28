package com.volkdem.cashdesc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.model.Order;
import com.common.model.Store;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.ui.IUnlockListener;
import com.volkdem.cashdesc.ui.IViewFinder;
import com.volkdem.cashdesc.ui.SreenLocker;
import com.volkdem.cashdesc.utils.BaseJsonRequest;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.util.Collection;


public class ScanShopCodeActivity extends ScanCodeActivity implements SurfaceHolder.Callback, IViewFinder, IUnlockListener {
    private static final String TAG = Const.TAG + ScanShopCodeActivity.class.getSimpleName();

    private SreenLocker screenLocker;
    private RequestQueue requestQueue;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.store);

        screenLocker = new SreenLocker(this);
        requestQueue = Volley.newRequestQueue(this);


        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final RelativeLayout mainMenu = (RelativeLayout) findViewById( R.id.main_menu );

        final ListView mainMenuList = (ListView) findViewById(R.id.main_menu_list);
        mainMenuList.setAdapter( new MainMenuAdapter() );


        drawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, drawerLayout, R.string.button_open_browser, R.string.button_add_contact ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("opened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle("closed");
            }
        });


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
