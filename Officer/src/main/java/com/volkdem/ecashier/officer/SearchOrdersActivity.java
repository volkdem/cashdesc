package com.volkdem.ecashier.officer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.model.Order;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tagmanager.TagManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import officer.cashdesc.volkdem.com.officer.R;

public class SearchOrdersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private static final String TAG = SearchOrdersActivity.class.getName();
    private OrderListAdapter orderListAdapter = null;
    private OrdersSearchCriteria searchCriteria = new OrdersSearchCriteria();

    private static final String AUTHORITY = "com.volkdem.ecashier.officer.provider";
    private static final String ACCOUNT_TYPE = "com.volkdem.ecashier.officer";
    private static final String ACCOUNT = "officer";
    private Account account;
    private Timer timer = new Timer();
    private Handler hander = new Handler();
    private OrdersDatabase ordersDB;
    private ContentResolver contentResolver;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = createOrdersSyncAccount(this);

        contentResolver = getContentResolver();
        contentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, 1L);


        setContentView(R.layout.activity_search_orders);

        handleIntent(getIntent());

        RecyclerView orderListView = (RecyclerView) findViewById(R.id.order_list);
        orderListView.setHasFixedSize(true);

        RecyclerView.LayoutManager orderListLayoutManager = new LinearLayoutManager(this);
        orderListView.setLayoutManager(orderListLayoutManager);


        ordersDB = OrdersDatabase.getDatabase(this);
        List<Order> orders = OrderFactory.generateOrders(50, 5);
        ordersDB.addOrders(orders);


        orderListAdapter = new OrderListAdapter(ordersDB, searchCriteria);
        orderListView.setAdapter(orderListAdapter);

        onQueryChanged("");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "Called by timer");
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Called by handler");
                        SearchOrdersActivity.this.forseSyncRequest();
                    }
                });
            }
        }, 0, 10000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_orders_options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setClickable(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnClickListener(this);
        moveSearchIconFromBeginToEnd(searchView);

        return true;
    }

    private void moveSearchIconFromBeginToEnd(SearchView searchView) {
        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        ViewGroup parent = (ViewGroup) searchIcon.getParent();
        parent.removeView(searchIcon);
        parent.addView(searchIcon);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "handle search, query=" + query);
            onQueryChanged(query);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryChanged(query);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return onQueryChanged(query);
    }

    private boolean onQueryChanged(String query) {
        searchCriteria.setPaymentCode(query);
        orderListAdapter.setSearchCriteria(searchCriteria);

        TextView approvedOrdersCountView = (TextView) findViewById(R.id.payed_orders_count);
        approvedOrdersCountView.setText(ordersDB.getPayedOrdersCount(searchCriteria).toString());

        TextView expiredOrdersCountView = (TextView) findViewById(R.id.expired_orders_count);
        expiredOrdersCountView.setText(ordersDB.getExpiredOrdersCount(searchCriteria).toString());
        return true;
    }

    private static Account createOrdersSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {

        } else {
            // TODO: report to server or identify WHY?
            Log.e(TAG, "Can not add account");
            //throw new RuntimeException( "Can not add new account");
        }

        return newAccount;
    }

    private void forseSyncRequest() {
        Bundle settingsBugnle = new Bundle();
        settingsBugnle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBugnle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, AUTHORITY, settingsBugnle);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onSearchClick()");
        RelativeLayout searchPanel = (RelativeLayout) findViewById(R.id.extended_search);

        if (searchPanel.getVisibility() == View.GONE) {
            searchPanel.setVisibility(View.VISIBLE);
        } else {
            searchPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SearchOrders Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.volkdem.ecashier.officer/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SearchOrders Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.volkdem.ecashier.officer/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
/*s


*/