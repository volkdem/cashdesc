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

public class SearchOrdersActivity extends AppCompatActivity {
    private static final String TAG = SearchOrdersActivity.class.getName();
    private OrderListAdapter orderListAdapter = null;

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
        List< Order > orders = OrderFactory.generateOrders( 10, 5 );

        OrdersDatabase ordersDB = new OrdersDatabase( this );
        ordersDB.addOrders( orders );

        OrderListAdapter orderListAdapter = new OrderListAdapter( ordersDB );
        orderListView.setAdapter( orderListAdapter );

        orderListAdapter.notifyDataSetChanged();



        OrdersSearchCriteria searchCriteria = new OrdersSearchCriteria();
        int position = 0;
        for( ; position < orders.size(); position ++ ) {
            Order order = orders.get( position );
            Log.d( TAG, "Test id = " + order.getId() );
            if( order.getId().length() > 1 ) {
                Log.d( TAG, "Satisfing id = " + order.getId() + " with position = " + position );
                break;
            }
        }
        Long orderId = Long.valueOf( orders.get( position ).getId().substring( 0, 1 ) );
        Log.d( TAG, "Found orderId = " + orderId );
        //searchCriteria.setPaymentCode( String.valueOf( orderId ) );
        Cursor orderCursor = ordersDB.getOrders( searchCriteria );
        Order order = OrderMapper.getOrder( orderCursor );
        Log.d( TAG, "Order: " + order  );
        Map<Product, Integer> products = OrderMapper.getProducts( ordersDB.getProducts( Long.valueOf( order.getId() ) ) );
        Log.d( TAG, "\t\tproducts: " + products );


        /*
        OrdersSearchCriteria searchCriteria = new OrdersSearchCriteria();
        Cursor cursor = ordersDB.getOrders( searchCriteria );
        */
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

        return true;
    }

    private void handleIntent(Intent intent ) {
        if (Intent.ACTION_SEARCH.equals( intent.getAction() ) ) {
            String query = intent.getStringExtra( SearchManager.QUERY );
        }
    }
}
/*s


*/