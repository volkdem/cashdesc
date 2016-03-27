package com.volkdem.cashdesc;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.model.Product;
import com.common.model.Store;
import com.google.zxing.Result;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.ui.IViewFinder;
import com.volkdem.cashdesc.ui.SreenLocker;
import com.volkdem.cashdesc.utils.BaseJsonRequest;
import com.volkdem.cashdesc.utils.Const;
import com.volkdem.cashdesc.utils.StaticContainer;

import java.text.MessageFormat;
import java.util.Observable;
import java.util.Observer;

public class ScanProdcutActivity extends ScanCodeActivity implements IViewFinder, Observer {
    private static final String TAG = Const.TAG + ScanProdcutActivity.class.getSimpleName();

    private SreenLocker screenLocker;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        screenLocker = new SreenLocker( this );

        OrderWrapper order = StaticContainer.getOrder();
        Store store = order.getStore();
        TextView storeInfoView = (TextView) findViewById( R.id.store_info );
        String storeInfo =  getResources().getString( R.string.store_info, new Object[] { store.getName(), store.getAddress() } );
        storeInfoView.setText( storeInfo );
        ImageView cartImage = (ImageView) findViewById(R.id.cart_image );
        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( StaticContainer.getOrder().getTotalSize() == 0 ) {
                    Toast.makeText( ScanProdcutActivity.this, R.string.cart_is_emtpy, Toast.LENGTH_LONG ).show();
                    return;
                }

                goToThePaymentConfirmationAcitivity();
            }
        });
        order.addObserver( this );

        // to update cart information if user comes to this screen from the PaymentConfirmation screen by back button click
        order.notifyObservers();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_scan_prodcut;
    }

    @Override
    protected void onDecode(final Result rawResult, Bitmap barcode, float scaleFactor) {
        Log.d( TAG, "onDecode(). Scanned barcode=" + rawResult.getText() );

        final OrderWrapper order = StaticContainer.getOrder();

        if( isLimitExceeded( order )) {
            showLimitExceedingMessage();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue( this );
        String url = getProductUrl( order.getStore().getStoreId(), rawResult.getText() );

        BaseJsonRequest<Product> productRequest = new BaseJsonRequest<Product>( Product.class, Request.Method.GET, url, null,
                new Response.Listener<Product>() {

                    @Override
                    public void onResponse(Product product) {
                        order.addProduct( product );
                        Log.d( TAG, "Found prodcut: " + product.getProductName() );

                        Log.i( TAG, "orderSize=" + order.getTotalSize() );

                        TextView productNameView = (TextView) findViewById( R.id.product_name );
                        productNameView.setText( product.getProductName() );

                        TextView productPriceView = (TextView) findViewById( R.id.product_price );
                        String price = product.getPrice().toString() + " "  + Const.CURRENCY;
                        productPriceView.setText( price );

                        screenLocker.unlockScreen();

                        if(isLimitExceeded(order)) {
                            showLimitExceedingMessage();
                            goToThePaymentConfirmationAcitivity();
                        } else {
                            restartPreviewAfterDelay(0L);
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO user message from the strings.xml
                        screenLocker.unlockScreen();
                        Toast.makeText(ScanProdcutActivity.this, "Error on getting product + " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        restartPreviewAfterDelay(0L);
                    }
                });

        screenLocker.lockScreen();
        requestQueue.add( productRequest );
    }

    private void showLimitExceedingMessage() {
        Toast.makeText( this, getResources().getString( R.string.limit_is_exceeded, Const.MAX_ORDER_SIZE ), Toast.LENGTH_LONG ).show();
    }

    private boolean isLimitExceeded(OrderWrapper order) {
        return order.getTotalSize() == Const.MAX_ORDER_SIZE;
    }

    private String getProductUrl(Long storeId, String productBarCode ) {
        return MessageFormat.format("{0}product?storeId={1}&productBarcode={2}", Const.URL, storeId, productBarCode );
    }

    private void goToThePaymentConfirmationAcitivity() {
        Intent paymentConfirmationActivityIntent = new Intent(ScanProdcutActivity.this, PaymentConfirmationActivity.class);
        startActivity(paymentConfirmationActivityIntent);
    }

    @Override
    public void update(Observable observable, Object data) {
        OrderWrapper order = (OrderWrapper) observable;

        TextView cartSizeView = (TextView)findViewById( R.id.cart_size );
        cartSizeView.setText( String.valueOf( order.getTotalSize() ) );

        TextView cartCostView = (TextView)findViewById( R.id.cart_cost );
        cartCostView.setText( String.valueOf( order.getCost() + Const.CURRENCY ) );
    }

}
