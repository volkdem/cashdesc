package officer.cashdesc.volkdem.com.officer;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

public class SearchOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_orders);

        handleIntent( getIntent() );
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
