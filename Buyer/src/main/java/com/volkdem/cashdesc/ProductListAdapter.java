package com.volkdem.cashdesc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.common.model.Order;
import com.common.model.Product;
import com.volkdem.cashdesc.model.OrderWrapper;

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

        Product product = order.getItem( position );

        View prodcutView = inflater.inflate( R.layout.layout_product, parent, false );
        TextView nameView = ( TextView )prodcutView.findViewById( R.id.product_name );
        nameView.setText( product.getProductName() );
        TextView priceView = (TextView )prodcutView.findViewById( R.id.product_price );
        priceView.setText( product.getPrice().setScale( 2, RoundingMode.HALF_UP ).toString() );

        return prodcutView;
    }

    @Override
    public void update(Observable observable, Object data) {
        notifyDataSetChanged();
    }
}
