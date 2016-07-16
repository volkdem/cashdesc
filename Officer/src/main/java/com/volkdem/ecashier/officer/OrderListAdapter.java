package com.volkdem.ecashier.officer;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.model.Order;
import com.common.model.Product;

import java.util.Calendar;
import java.util.Map;

import officer.cashdesc.volkdem.com.officer.R;
import com.volkdem.ecashier.officer.utils.DateUtil;

/**
 * Created by Evgeny on 21.05.2016.
 */
public class OrderListAdapter extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {
    private static final String TAG = OrderListAdapter.class.getName();
    private static final int EXPIRATION_TIME = 30; // in minutes
    private final int NONE = -1;
    private int expanded = NONE;
    private OrdersDatabase db;
    private OrdersSearchCriteria searchCriteria = null;

    public OrderListAdapter( OrdersDatabase db, OrdersSearchCriteria searchCriteria) {
        super(db.getOrders( searchCriteria ) );
        this.searchCriteria = searchCriteria;
        this.db = db;
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, final Cursor cursor, final int position) {
        Log.d( TAG, "onBindViewHolder: bind holder to position: " + position );
        final View itemView = holder.itemView;
        final Order order = OrderMapper.getOrder( cursor );
        Log.d( TAG, " id=" + order.getId()  + ", code=" + order.getPaymentCode() + ", status=" + order.isCheckStatus() );

        itemView.setBackgroundResource( getOrderColor( order ) );

        TextView paymentCodeView = (TextView) itemView.findViewById( R.id.payment_code );

        Log.d( TAG, "onBindViewHolder: bind holder to id: " + order.getId() );
        order.setProducts( OrderMapper.getProducts( db.getProducts( Long.valueOf( order.getId() ) ) ) );
        // TODO: replace id to paymentCode
        paymentCodeView.setText(String.valueOf(order.getPaymentCode()));

        TextView paymentDateView = (TextView) itemView.findViewById( R.id.payment_date );
        paymentDateView.setText( DateUtil.format( order.getPaymentDate() ) );

        TextView orderSize = (TextView) itemView.findViewById( R.id.order_size );
        orderSize.setText( String.valueOf( order.getTotalSize() ) );

        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrCollapse( position );
            }
        };

        itemView.setOnClickListener( itemClickListener );

        View footer = itemView.findViewById( R.id.order_footer );

        // Fill order footer
        if( holder.getItemViewType() == ViewType.EXPANDED.getCode() ) {
            footer.setVisibility( View.VISIBLE );

            LinearLayout productListView = (LinearLayout) itemView.findViewById( R.id.product_list );
            productListView.removeAllViews();
            for(Map.Entry<Product, Integer > entry: order.getProducts().entrySet() ) {
                LayoutInflater inflater = LayoutInflater.from( itemView.getContext() );
                View productView = inflater.inflate(R.layout.product_item, null);

                Product product = entry.getKey();

                TextView productNameView = (TextView) productView.findViewById( R.id.product_name );
                productNameView.setText( product.getProductName() );

                TextView productCountView = (TextView) productView.findViewById( R.id.product_count );
                productCountView.setText( String.valueOf( order.getCount( product ) ) );

                productListView.addView( productView );
            }

            Button checkButton = (Button) itemView.findViewById( R.id.check_product );
            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition( position );
                    // TODO invert order's check status (remove hardcoded 'true')
                    setOrderCheckStatus( Long.valueOf( order.getId() ), position, true );
                }
            });

        } else {
            footer.setVisibility( View.GONE );
        }
    }

    private int getOrderColor(Order order) {
        if ( order.isCheckStatus() ) {
            return R.color.approvedOrder;
        } else if( isOrderExpired( order ) ) {
            return R.color.expiredOrder;
        } else {
            return R.color.newOrder;
        }
    }


    private boolean isOrderExpired(Order order) {
        Calendar expiredTime = getExpiredTime();
        Calendar payDate = Calendar.getInstance();
        payDate.setTime( order.getPaymentDate() );
        return expiredTime.compareTo( payDate ) > 0; // NOTE: striclty more (syncrhonized with OrdersSearchCriteria from/to interpretation in the OrdersDatabase requests
    }

    public static Calendar getExpiredTime() {
        Calendar cur = Calendar.getInstance();
        cur.add(Calendar.MINUTE, -EXPIRATION_TIME );
        return cur;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        ViewGroup orderItemView = (ViewGroup) inflater.inflate( R.layout.order_item, parent, false );
        RecyclerView.ViewHolder viewHolder = new ViewHolder( orderItemView );
        Log.d( TAG, "onCreateViewHolder: new holder with type: " + viewType );
        return viewHolder;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }


    private void setOrderCheckStatus(Long id, int position, boolean checkStatus ) {
        db.setCheckStatus( id, checkStatus );
        this.changeCursor( db.getOrders( searchCriteria ) );
        this.notifyItemRemoved( position );
        if( expanded == position ) {
            expanded = NONE;
        }

    }


    @Override
    public int getItemViewType(int position) {
        if( position == expanded ) {
            return ViewType.EXPANDED.getCode();
        } else {
            return ViewType.COLLAPSED.getCode();
        }
    }

    private void expandOrCollapse(int position ) {
        Log.d( TAG, "expandOrCollapse position: " + position );
        Log.d( TAG, "expandOrCollapse expaned: " + expanded);
        Log.d( TAG, "expandOrCollapse itemCount: " + getItemCount() );

        /* Code fixes error
        * Error description:
        * 1. Confirm any not last order.
        * 2. Confirm last order
        * 3. Confirm any not last order.
        * 4. Try to expaned last order. It is not expaned.
        * TODO: Fix error basing on the workflow (right way).
        */

        if (position == getItemCount() ) {
            Log.e( TAG, "Position value is out of the array: " + position + ", - correct it to last array's element" );
            position = getItemCount() - 1;
        }

        if( expanded == position ) {
            expanded = NONE;
        } else {
            if( expanded != NONE ) {
                notifyItemChanged( expanded );
            }
            expanded = position;
        }

        notifyItemChanged( position );
    }

    /*
    @Override
    public int getItemCount() {
        return orders.size();
    }
    */

    enum ViewType {
        EXPANDED( 1 ),
        COLLAPSED( 2 );

        private ViewType(int code) {
            this.code = code;
        }

        private int code;

        public int getCode() {
            return code;
        }

        public static ViewType getViewType(int code) {
            for( ViewType value: values() ) {
                if( value.getCode() == code ) {
                    return value;
                }
            }

            throw new RuntimeException( "Undefined view type code: " + code );
        }

    }

    public void setSearchCriteria(OrdersSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
        this.changeCursor( db.getOrders( searchCriteria ) );
        notifyDataSetChanged();
    }
}
