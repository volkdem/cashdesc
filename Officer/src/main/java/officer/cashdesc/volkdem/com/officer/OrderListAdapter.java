package officer.cashdesc.volkdem.com.officer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.model.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Evgeny on 21.05.2016.
 */
public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Order> orders;

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
        View orderItemView = inflater.inflate( R.layout.order_item, parent, false );
        RecyclerView.ViewHolder viewHolder = new ViewHolder( orderItemView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO place as adapters's property
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd.MM.yyyy HH:mm");
        View itemView = holder.itemView;
        TextView paymentCodeView = (TextView) itemView.findViewById( R.id.payment_code );
        Order order = orders.get( position );
        // TODO: replace id to paymentCode
        paymentCodeView.setText( order.getId() );

        TextView paymentDateView = (TextView) itemView.findViewById( R.id.payment_date );
        paymentDateView.setText( dateFormat.format( order.getPaymentDate() ) );

        TextView orderSize = (TextView) itemView.findViewById( R.id.order_size );
        orderSize.setText( String.valueOf( order.getTotalSize() ) );
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


}
