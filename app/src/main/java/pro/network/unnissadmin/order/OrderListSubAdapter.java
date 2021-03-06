package pro.network.unnissadmin.order;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pro.network.unnissadmin.R;
import pro.network.unnissadmin.app.Appconfig;
import pro.network.unnissadmin.product.Product;

import static pro.network.unnissadmin.app.Appconfig.decimalFormat;


public class OrderListSubAdapter extends RecyclerView.Adapter<OrderListSubAdapter.MyViewHolder> {

    private Context mainActivityUser;
    private ArrayList<Product> myorderBeans;
    int selectedPosition = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView subtitle;
        private  final ImageView product_image;


        public MyViewHolder(View view) {
            super((view));
            title = view.findViewById(R.id.title_one);
            subtitle = view.findViewById(R.id.title_two);
            product_image = view.findViewById(R.id.product_image);


        }
    }

    public OrderListSubAdapter(Context mainActivityUser, ArrayList<Product> myorderBeans) {
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

    public OrderListSubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_list_sub, parent, false);

        return new OrderListSubAdapter.MyViewHolder(itemView);
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
                "=" + "???" + decimalFormat.format(startValue) + ".00"+"\nSize :"+myorderBean.getSize());
    }

    public int getItemCount() {
        return myorderBeans.size();
    }

}


