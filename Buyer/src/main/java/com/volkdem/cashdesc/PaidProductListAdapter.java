package com.volkdem.cashdesc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.common.model.Product;
import com.volkdem.cashdesc.model.OrderWrapper;
import com.volkdem.cashdesc.utils.Const;

import java.math.RoundingMode;

/**
 * Created by Evgeny on 07.05.2016.
 */
public class PaidProductListAdapter extends BaseAdapter {

    private OrderWrapper order;

    public PaidProductListAdapter(OrderWrapper orderWrapper) {
        this.order = orderWrapper;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View prodcutView = convertView;

        if( prodcutView == null ) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE );
            prodcutView = inflater.inflate( R.layout.layout_paid_product, null );
        }

        final Product product = order.getItem( position );

        // TODO check for too long product name
        TextView nameView = ( TextView )prodcutView.findViewById( R.id.product_name );
        nameView.setText( product.getProductName() );

        TextView countView = ( TextView )prodcutView.findViewById( R.id.product_count );
        String count = String.valueOf( order.getCount( product ) );
        countView.setText( count + " x "); // TODO move to layout

        TextView priceView = (TextView )prodcutView.findViewById( R.id.product_price );
        // TODO move currency to layout
        priceView.setText( product.getPrice().setScale( 2, RoundingMode.HALF_UP ).toString() + " " + Const.CURRENCY );

        return prodcutView;
    }
}
