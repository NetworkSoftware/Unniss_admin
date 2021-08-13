package pro.network.freshcatchadmin.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pro.network.freshcatchadmin.R;
import pro.network.freshcatchadmin.app.Appconfig;

/**
 * Created by ravi on 16/11/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder>
        implements Filterable {
    StatusListener statusListener;
    private final Context context;
    private List<Order> orderList;
    private List<Order> orderListFiltered;
    private final ContactsAdapterListener listener;

    public OrderAdapter(Context context, List<Order> orderList, ContactsAdapterListener listener, StatusListener statusListener) {
        this.context = context;
        this.listener = listener;
        this.orderList = orderList;
        this.orderListFiltered = orderList;
        this.statusListener = statusListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Order order = orderListFiltered.get(position);
        holder.price.setText(order.getPrice());
        holder.quantity.setText(order.getQuantity());
        holder.order_id.setText(order.getId());
        holder.status.setText(order.getStatus());
        holder.phone.setText(order.getPhone());
        holder.name.setText(order.getName());
        holder.address.setText(order.getAddress());
        holder.reason.setText(order.getReson());
        holder.total_coupon.setText(order.getTotalAmt());
        holder.cashback.setText("- ₹" + order.getCashback());
        holder.orderedOn.setText(Appconfig.convertTimeToLocal(order.createdon));
        if (order.getStatus().equalsIgnoreCase("ordered")) {
            holder.deliveredBtn.setVisibility(View.VISIBLE);
            holder.wallet.setVisibility(View.VISIBLE);
        } else {
            holder.deliveredBtn.setVisibility(View.GONE);
            holder.wallet.setVisibility(View.GONE);

        }

        if (!order.getStatus().equalsIgnoreCase("canceled")
                && order.getStatus().equalsIgnoreCase("ordered")) {
            holder.cancalOrder.setVisibility(View.VISIBLE);
        } else {
            holder.cancalOrder.setVisibility(View.GONE);
        }

        OrderListSubAdapter OrderListAdapter = new OrderListSubAdapter(context, order.productBeans);
        final LinearLayoutManager addManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.cart_sub_list.setLayoutManager(addManager1);
        holder.cart_sub_list.setAdapter(OrderListAdapter);

        holder.deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusListener.onDeliveredClick(order.id);
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusListener.onWhatsAppClick(order.phone);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusListener.onCallClick(order.phone);
            }
        });
        holder.cancalOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusListener.onCancelClick(order.id);
            }
        });

        holder.single_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusListener.onProduct(order);
            }
        });
        holder.wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusListener.onWallet(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    orderListFiltered = orderList;
                } else {
                    List<Order> filteredList = new ArrayList<>();
                    for (Order row : orderList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String val = row.getName();
                        if (val.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else if (row.getPhone().contains(charString.toLowerCase())) {
                            filteredList.add(row);

                        }
                    }

                    orderListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orderListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderListFiltered = (ArrayList<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyData(List<Order> orderList) {
        this.orderListFiltered = orderList;
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Order order);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, cashback,
                status, quantity, phone, orderedOn, address,total_coupon, reason, order_id;
        public ImageView thumbnail;
        public RecyclerView cart_sub_list;
        ImageView deliveredBtn, whatsapp, call, cancalOrder, wallet;
        LinearLayout single_order;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            orderedOn = view.findViewById(R.id.orderedOn);
            single_order = view.findViewById(R.id.single_order);
            price = view.findViewById(R.id.price);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            status = view.findViewById(R.id.status);
            quantity = view.findViewById(R.id.quantity);
            thumbnail = view.findViewById(R.id.thumbnail);
            cart_sub_list = view.findViewById(R.id.cart_sub_list);
            deliveredBtn = view.findViewById(R.id.deliveredBtn);
            whatsapp = view.findViewById(R.id.whatsapp);
            cancalOrder = view.findViewById(R.id.cancalOrder);
            call = view.findViewById(R.id.call);
            address = view.findViewById(R.id.address);
            reason = view.findViewById(R.id.reason);
            order_id = view.findViewById(R.id.order_id);
            wallet = view.findViewById(R.id.wallet);
            cashback = view.findViewById(R.id.cashback);
            total_coupon = view.findViewById(R.id.total_coupon);
        }
    }
}
