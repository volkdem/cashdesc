package officer.cashdesc.volkdem.com.officer;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.common.model.Order;
import com.common.model.Product;
import java.util.List;
import java.util.Map;

public class SearchOrdersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private static final String TAG = SearchOrdersActivity.class.getName();
    private OrderListAdapter orderListAdapter = null;
    private OrdersSearchCriteria searchCriteria = new OrdersSearchCriteria();

    private static final String AUTHORITY = "officer.cashdesc.volkdem.com.officer.provider";
    private static final String ACCOUNT_TYPE = "officer.cashdesc.volkdem";
    private static final String ACCOUNT = "officer";
    private Account account;


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

        LinearLayout extendedSearch = (LinearLayout) findViewById(R.id.extended_search );
        extendedSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout searchPanel = (LinearLayout) findViewById( R.id.search_panel );
                if(  searchPanel.getVisibility() == View.GONE ) {
                    searchPanel.setVisibility( View.VISIBLE );
                } else {
                    searchPanel.setVisibility( View.GONE );
                }
            }
        });

        // TODO: remove stub
        List< Order > orders = OrderFactory.generateOrders( 100, 5 );

        OrdersDatabase ordersDB = new OrdersDatabase( this );
        ordersDB.addOrders( orders );

        orderListAdapter = new OrderListAdapter( ordersDB, searchCriteria );
        orderListView.setAdapter( orderListAdapter );

        orderListAdapter.notifyDataSetChanged();
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
        searchView.setOnQueryTextListener( this );

        return true;
    }

    private void handleIntent(Intent intent ) {
        if (Intent.ACTION_SEARCH.equals( intent.getAction() ) ) {

            String query = intent.getStringExtra( SearchManager.QUERY );
            Log.d( TAG, "handle search, query=" + query );
            searchCriteria.setPaymentCode( query );
            orderListAdapter.setSearchCriteria( searchCriteria );

            forseSyncRequest();
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchCriteria.setPaymentCode( query );
        orderListAdapter.setSearchCriteria( searchCriteria );
        forseSyncRequest();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query ) {
        searchCriteria.setPaymentCode( query );
        orderListAdapter.setSearchCriteria( searchCriteria );
        forseSyncRequest();
        return true;
    }

    private static Account createOrdersSyncAccount(Context context) {

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager)context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly( newAccount, null, null ) ) {

        } else {
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
}
/*s


*/