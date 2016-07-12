package com.volkdem.ecashier.officer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.model.Order;

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


    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = createOrdersSyncAccount(this);

        contentResolver = getContentResolver();
        contentResolver.setSyncAutomatically( account, AUTHORITY, true );
        ContentResolver.addPeriodicSync( account, AUTHORITY, Bundle.EMPTY, 1L );



        setContentView(R.layout.activity_search_orders);

        handleIntent( getIntent() );

        RecyclerView orderListView = (RecyclerView) findViewById( R.id.order_list );
        orderListView.setHasFixedSize( true );

        RecyclerView.LayoutManager orderListLayoutManager = new LinearLayoutManager( this );
        orderListView.setLayoutManager( orderListLayoutManager );


        final OrdersDatabase ordersDB = OrdersDatabase.getDatabase( this );
        List<Order> orders = OrderFactory.generateOrders( 20, 10 );
        ordersDB.addOrders( orders );


        orderListAdapter = new OrderListAdapter( ordersDB, searchCriteria );
        orderListView.setAdapter( orderListAdapter );

        orderListAdapter.notifyDataSetChanged();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d( TAG, "Called by timer");
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d( TAG, "Called by handler");
                        SearchOrdersActivity.this.forseSyncRequest();
                    }
                });
            }
        }, 0, 10000);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent( intent );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.search_orders_options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem( R.id.search ).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo( getComponentName() ) );
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setClickable(true);
        searchView.setOnQueryTextListener( this );
        searchView.setOnClickListener(this);
        return true;
    }

    private void handleIntent(Intent intent ) {
        if (Intent.ACTION_SEARCH.equals( intent.getAction() ) ) {

            String query = intent.getStringExtra( SearchManager.QUERY );
            Log.d( TAG, "handle search, query=" + query );
            onQueryChanged(query);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return onQueryChanged( query );
    }

    @Override
    public boolean onQueryTextChange(String query ) {
        return onQueryChanged( query );
    }

    private boolean onQueryChanged(String query) {
        searchCriteria.setPaymentCode( query );
        orderListAdapter.setSearchCriteria( searchCriteria );
        return true;
    }

    private static Account createOrdersSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager)context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly( newAccount, null, null ) ) {

        } else {
            // TODO: report to server or identify WHY?
            Log.e( TAG, "Can not add account");
            //throw new RuntimeException( "Can not add new account");
        }

        return newAccount;
    }

    private void forseSyncRequest() {
        Bundle settingsBugnle = new Bundle();
        settingsBugnle.putBoolean( ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBugnle.putBoolean( ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync( account, AUTHORITY, settingsBugnle );
    }

    @Override
    public void onClick(View v) {
        Log.d( TAG, "onSearchClick()");
        RelativeLayout searchPanel = (RelativeLayout) findViewById(R.id.extended_search );

        if(  searchPanel.getVisibility() == View.GONE ) {
            searchPanel.setVisibility( View.VISIBLE );
        } else {
            searchPanel.setVisibility( View.GONE );
        }
    }
}
/*s


*/