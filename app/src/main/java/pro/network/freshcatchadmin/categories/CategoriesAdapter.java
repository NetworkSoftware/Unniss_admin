package pro.network.freshcatchadmin.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pro.network.freshcatchadmin.R;
import pro.network.freshcatchadmin.app.Appconfig;
import pro.network.freshcatchadmin.app.GlideApp;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {
    private Context context;
    private List<Categories> categoriesList;
    private CategoriesClick bannerClick;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail, cancel;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);


            thumbnail = view.findViewById(R.id.thumbnail);
            cancel = view.findViewById(R.id.cancel);
            title = view.findViewById(R.id.title);

        }
    }


    public CategoriesAdapter(Context context, List<Categories> categoriesList, MainActivityCategories bannerClick) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.bannerClick = bannerClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Categories categories = categoriesList.get(position);

        holder.title.setText(categories.title);
        GlideApp.with(context)
                .load(Appconfig.getResizedImage(categories.getImage(), true))
                .placeholder(R.drawable.fresh_icon)
                .into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onItemClick(position);
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerClick.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }


    public void notifyData(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }
}
