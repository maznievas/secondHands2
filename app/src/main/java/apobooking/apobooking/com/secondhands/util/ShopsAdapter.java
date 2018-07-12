package apobooking.apobooking.com.secondhands.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by procreationsmac on 03/07/2018.
 */

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ViewHolder> {

    List<Shop> shopList;
    Context context;

    public ShopsAdapter(Context context) {
        shopList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        holder.shopNameTextView.setText(shop.getName());
        holder.shopAdressTextView.setText(shop.getAddress());
        holder.updateDayTextView.setText(context.getString(DayDetectHelper.detectDay(shop.getUpdateDay())));
    }

    @Override
    public int getItemCount() {
        if (shopList != null)
            return shopList.size();
        else
            return 0;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
        notifyDataSetChanged();
    }

    public void clear() {
        shopList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shopImage)
        ImageView shopImage;

        @BindView(R.id.shopNameTextView)
        TextView shopNameTextView;

        @BindView(R.id.adressTextView)
        TextView shopAdressTextView;

        @BindView(R.id.updateDayTextView)
        TextView updateDayTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
