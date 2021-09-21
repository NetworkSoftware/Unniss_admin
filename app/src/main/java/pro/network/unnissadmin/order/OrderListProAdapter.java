package pro.network.unnissadmin.order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pro.network.unnissadmin.R;
import pro.network.unnissadmin.app.Appconfig;
import pro.network.unnissadmin.product.Product;
import pro.network.unnissadmin.product.ProductUpdate;

import static pro.network.unnissadmin.app.Appconfig.decimalFormat;


public class OrderListProAdapter extends RecyclerView.Adapter<OrderListProAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Product> myorderBeans;
    SharedPreferences preferences;
    int selectedPosition = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView subtitle;
        private  final ImageView product_image;
        LinearLayout items;


        public MyViewHolder(View view) {
            super((view));
            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
            product_image = view.findViewById(R.id.product_image);
            items = view.findViewById(R.id.items);


        }
    }

    public OrderListProAdapter(Context mainActivityUser, ArrayList<Product> myorderBeans) {
        this.mainActivityUser = mainActivityUser;
        this.myorderBeans = myorderBeans;
    }

    public void notifyData(ArrayList<Product> myorderBeans) {
        this.myorderBeans = myorderBeans;
        notifyDataSetChanged();
    }

    public void notifyData(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public OrderListProAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_order_item, parent, false);

        return new OrderListProAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Product myorderBean = myorderBeans.get(position);
        holder.title.setText(myorderBean.getName());
        if (myorderBean.getImage() != null && myorderBean.getImage().length() > 0) {

            Picasso.with(mainActivityUser)
                    .load(Appconfig.getResizedImage(myorderBean.getImage(), true))
                    .placeholder(R.drawable.unniss)
                    .error(R.drawable.unniss)
                    .into(holder.product_image);
        }
        String qty = myorderBean.getQty();
        try {
            if (qty == null || !qty.matches("-?\\d+(\\.\\d+)?")) {
                qty = "1";
            }
        } catch (Exception e) {

        }
        float startValue = Float.parseFloat(myorderBean.getPrice()) * Float.parseFloat(qty);
        holder.subtitle.setText(myorderBean.getQty() + "*" + myorderBean.getPrice() + "/" +
                myorderBean.getRqty() + " " + myorderBean.getRqtyType() +
                "\nTotal Amt = " + "₹" + decimalFormat.format(startValue) + ".00"+"\nSize :"+myorderBean.getSize());

        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mainActivityUser, ProductUpdate.class);
                intent.putExtra("isServer",true);
                intent.putExtra("id",myorderBean.getId());
                mainActivityUser.startActivity(intent);
            }
        });

    }

    public int getItemCount() {
        return myorderBeans.size();
    }

}


