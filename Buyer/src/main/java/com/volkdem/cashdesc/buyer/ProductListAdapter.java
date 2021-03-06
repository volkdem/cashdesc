package com.volkdem.cashdesc.buyer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.common.model.Product;
import com.volkdem.cashdesc.R;
import com.volkdem.cashdesc.buyer.model.OrderWrapper;
import com.volkdem.cashdesc.buyer.utils.Const;

import java.math.RoundingMode;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Evgeny on 19.03.2016.
 */
public class ProductListAdapter extends BaseAdapter implements Observer{
    private static final String TAG = ProductListAdapter.class.getSimpleName();

    private OrderWrapper order;

    public ProductListAdapter(OrderWrapper order) {
        this.order = order;
    }

    @Override
    public int getCount() {
        return order.getProductTypeSize();
    }

    @Override
    public Object getItem(int position) {
        return order.getItem( position );
    }

    @Override
    public long getItemId(int position) {
        return order.getItemId( position );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE );

        Log.d( TAG, "getView( position = " + position + " )");

        final Product product = order.getItem( position );

        View prodcutView = inflater.inflate( R.layout.layout_product, parent, false );
        // TODO check for too long product name
        TextView nameView = ( TextView )prodcutView.findViewById( R.id.product_name );
        nameView.setText( product.getProductName() );

        TextView countView = ( TextView )prodcutView.findViewById( R.id.product_count );
        String count = String.valueOf( order.getCount( product ) );
        countView.setText( count + "x "); // TODO move to layout

        TextView priceView = (TextView )prodcutView.findViewById( R.id.product_price );
        // TODO move currency to layout
        priceView.setText( product.getPrice().setScale( 2, RoundingMode.HALF_UP ).toString() + " " + Const.CURRENCY );

        Button removeButton = (Button) prodcutView.findViewById(R.id.remove_product);
        removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order.removeProduct( product );
                    ProductListAdapter.this.notifyDataSetChanged();
                }
            });

        return prodcutView;
    }


    @Override
    public void update(Observable observable, Object data) {
        notifyDataSetChanged();
    }
}
