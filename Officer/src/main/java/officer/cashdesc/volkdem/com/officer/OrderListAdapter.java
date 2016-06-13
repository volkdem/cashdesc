package officer.cashdesc.volkdem.com.officer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.model.Order;
import com.common.model.Product;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Evgeny on 21.05.2016.
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = OrderListAdapter.class.getName();
    private final int NONE = -1;
    private List<Order> orders;
    private int expanded = NONE;

    public OrderListAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        ViewGroup orderItemView = (ViewGroup) inflater.inflate( R.layout.order_item, parent, false );
        RecyclerView.ViewHolder viewHolder = new ViewHolder( orderItemView );
        Log.d( TAG, "onCreateViewHolder: new holder with type: " + viewType );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d( TAG, "onBindViewHolder: bind holder to position: " + position );
        final View itemView = holder.itemView;
        TextView paymentCodeView = (TextView) itemView.findViewById( R.id.payment_code );
        Order order = orders.get( position );
        // TODO: replace id to paymentCode
        paymentCodeView.setText(String.valueOf(order.getId()));

        // TODO: move dataFormat to adapters's property
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy HH:mm");
        TextView paymentDateView = (TextView) itemView.findViewById( R.id.payment_date );
        paymentDateView.setText( dateFormat.format( order.getPaymentDate() ) );

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
        ImageView arrowImage = (ImageView) itemView.findViewById( R.id.arrow );

        // Fill order footer
        if( holder.getItemViewType() == ViewType.EXPANDED.getCode() ) {
            footer.setVisibility( View.VISIBLE );
            arrowImage.setImageResource( R.drawable.arrow_up );

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
                    removeOrder( position );
                }
            });
        } else {
            footer.setVisibility( View.GONE );
            arrowImage.setImageResource( R.drawable.arrow_down );
        }
    }

    private void removeOrder( int position ) {
        orders.remove( position );
        notifyItemRemoved( position );
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

        /* Code fixes error
        * Error description:
        * 1. Confirm any not last order.
        * 2. Confirm last order
        * 3. Confirm any not last order.
        * 4. Try to expaned last order. It is not expaned.
        * TODO: Fix error basing on the workflow (right way).
        */
        if (position == orders.size() ) {
            Log.e( TAG, "Position value is out of the array: " + position + ", - correct it to last array's element" );
            position = orders.size() - 1;
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

    @Override
    public int getItemCount() {
        return orders.size();
    }

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
}
