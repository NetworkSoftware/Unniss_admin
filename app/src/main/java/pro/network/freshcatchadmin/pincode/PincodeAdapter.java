package pro.network.freshcatchadmin.pincode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.freshcatchadmin.R;
import pro.network.freshcatchadmin.categories.CategoriesClick;

public class PincodeAdapter extends RecyclerView.Adapter<PincodeAdapter.MyViewHolder> {
    private final Context context;
    private List<PincodeProduct> productListFiltered;
    private final PincodeAdapterListener listener;
    private PincodeClick bannerClick;

    public PincodeAdapter(MainActivityPincode mainActivityPincode, List<PincodeProduct> bannerList, PincodeAdapterListener listener ,PincodeClick bannerClick) {
        this.context = mainActivityPincode;
        this.listener = listener;
        this.productListFiltered = bannerList;
        this.bannerClick = bannerClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pincode_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PincodeProduct product = productListFiltered.get(position);
        holder.pincode.setText(product.getPincode());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onDeleteClick(position);
            }
        });

    }

    public void notifyData(List<PincodeProduct> productList) {
        this.productListFiltered = productList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    public interface PincodeAdapterListener {
        void onContactSelected(PincodeProduct product);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pincode;
        public ImageView cancel;


        public MyViewHolder(View view) {
            super(view);

            pincode = view.findViewById(R.id.pincode);
            cancel = view.findViewById(R.id.cancel);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(productListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
