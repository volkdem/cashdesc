package officer.cashdesc.volkdem.com.officer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.common.model.Order;
import com.common.model.Product;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Evgeny on 21.05.2016.
 */
public class ProductListAdapter extends BaseAdapter {
    private Order order;
    private Product[] productOrder;

    public ProductListAdapter(Order order) {
        this.order = order;
        productOrder = new Product[ order.getProducts().size() ];
        int i = 0;
        for( Product product: order.getProducts().keySet() ) {
            productOrder[ i++ ] = product;
        }
    }


    @Override
    public int getCount() {
        return productOrder.length;
    }

    @Override
    public Object getItem(int position) {
        return productOrder[ position ];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if( view == null ) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.product_item, null);
        }
        Product product = productOrder[ position ];

        TextView productNameView = (TextView) view.findViewById( R.id.product_name );
        productNameView.setText( product.getProductName() );

        TextView productCountView = (TextView) view.findViewById( R.id.product_count );
        productCountView.setText( String.valueOf( order.getCount( product ) ) );

        return view;
    }
}
