package com.volkdem.ecashier.officer;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.common.jackson.ProductDeserializer;
import com.common.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volkdem.ecashier.officer.communication.request.CustomRequst;
import com.volkdem.ecashier.officer.utils.Const;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeny on 07.07.2016.
 */
public class OrdersSyncAdapter extends AbstractThreadedSyncAdapter {
    private final static String TAG = OrdersSyncAdapter.class.getName();
    private Long lastId;
    private OrdersDatabase database = OrdersDatabase.getDatabase( getContext() );
    private RequestQueue requestQueue = Volley.newRequestQueue( getContext() );

    private ContentResolver contentResolver;

    public OrdersSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        this.contentResolver = context.getContentResolver();
    }

    public OrdersSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        this.contentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d( TAG, "onPerformSync() lastId: " + lastId );

        String url = null;
        if( lastId == null ) {
            url = getLoadOrdersUrl();
        } else {
            url = getLoadLastOrders(lastId);
        }
        Log.d( TAG, "onPerformSync() url: " + url );

        Request<ArrayList<Order>> request = new CustomRequst<ArrayList<Order>>(new TypeReference<ArrayList<Order>>() {
        }, Request.Method.GET, url, new ProductDeserializer(), new Response.Listener<ArrayList<Order>>() {
            @Override
            public void onResponse(ArrayList<Order> response) {
                Log.d(TAG, "onPerformSync() Returned order list size:" + ((response != null) ? response.size() : 0));
                List<Order> orders = (List<Order>) response;
                Log.d(TAG, "onPerformSync() values: OrderId:" + orders.get(0).getId());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                throw new RuntimeException(error.getMessage(), error.getCause());
            }
        }) {};

        Log.d(TAG, "onPerformSync() send request" );
        //requestQueue.add( request );
    }

    private String getLoadLastOrders(Long lastId) {
        return MessageFormat.format("{0}paidOrders/newAfterId?id={1}", Const.URL, lastId );
    }

    private String getLoadOrdersUrl() {
        return MessageFormat.format("{0}paidOrders/amount?ordersAmount={1}", Const.URL, Const.ORDERS_TABLE_SIZE );
    }

}
