package officer.cashdesc.volkdem.com.officer;

import android.app.SearchManager;
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

import com.common.model.Order;
import com.common.model.Product;
import java.util.List;
import java.util.Map;

public class SearchOrdersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private static final String TAG = SearchOrdersActivity.class.getName();
    private OrderListAdapter orderListAdapter = null;
    OrdersSearchCriteria searchCriteria = new OrdersSearchCriteria();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_orders);

        handleIntent( getIntent() );

        RecyclerView orderListView = (RecyclerView) findViewById( R.id.order_list );
        orderListView.setHasFixedSize( true );

        RecyclerView.LayoutManager orderListLayoutManager = new LinearLayoutManager( this );
        orderListView.setLayoutManager( orderListLayoutManager );

        // TODO: remove stub
        List< Order > orders = OrderFactory.generateOrders( 1000, 5 );

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
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        searchCriteria.setPaymentCode( query );
        orderListAdapter.setSearchCriteria( searchCriteria );
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query ) {
        searchCriteria.setPaymentCode( query );
        orderListAdapter.setSearchCriteria( searchCriteria );
        return true;
    }
}
/*s


*/