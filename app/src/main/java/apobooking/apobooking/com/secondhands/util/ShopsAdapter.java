package apobooking.apobooking.com.secondhands.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by procreationsmac on 03/07/2018.
 */

public class ShopsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Shop> shopList;
    Context context;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private ShopItemListener shopItemListener;

    public ShopsAdapter(Context context) {
        shopList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
       // return new ViewHolder(view);
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.shop_item, parent, false);
        viewHolder = new ViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                ViewHolder holder = (ViewHolder) holder1;
                Shop shop = shopList.get(position);

                holder.shopNameTextView.setText(shop.getName());
                holder.shopAdressTextView.setText(shop.getAddress());
                holder.updateDayTextView.setText(context.getString(DayDetectHelper.detectDay(shop.getUpdateDay())));

                holder.shopItemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shopItemListener.shopSelected(shop.getId());
                    }
                });

                int errorImage = R.color.recytclerViewItemColor;
                switch(shop.getName()){
                    case Const.ShopsName.ECONOM_CLASS:
                        errorImage = R.drawable.econom_logo;
                        break;
                }

//                Glide.with(context)
//                        .using(new FirebaseImageLoader())
//                        .load(shop.getImageReference())
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .placeholder(errorImage)
//                        .into(holder.shopImage);

//                Glide.with(context)
//                .using(new FirebaseImageLoader())
//                .load(shop.getImageReference())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        holder.progressBar.setVisibility(View.INVISIBLE);
//                        holder.shopImage.setImageDrawable(resource);
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        //super.onLoadFailed(e, errorDrawable);
//                        holder.progressBar.setVisibility(View.INVISIBLE);
//                        holder.shopImage.setImageResource(finalErrorImage);
//                    }
//                });

                int finalErrorImage = errorImage;
                shop.getImageReference().getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            // holder.progressBar.setVisibility(View.INVISIBLE);
                            Picasso.get().load(uri.toString()).into(holder.shopImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.progressBar.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    holder.progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        })
                        .addOnFailureListener(e -> {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                            Picasso.get().load(finalErrorImage).into(holder.shopImage);
                        });

                if(position == shopList.size() - 1)
                    SearchPropertiesFragment.allowToSearch = true;
                break;
            case LOADING:
                shopItemListener.scrollToBottom();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == shopList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
       // if(!isLoadingAdded) {
            shopList.add(new Shop());
            isLoadingAdded = true;
            Log.d("mLog", "Added. Sise: " + shopList.size());
            notifyDataSetChanged();
        //}
    }

    public void removeLoadingFooter() {
       // if(isLoadingAdded) {
            isLoadingAdded = false;

            int position = shopList.size() - 1;
            Shop item = shopList.get(position);

            if (item != null) {
                shopList.remove(position);
                Log.d("mLog", "Removed. Sise: " + shopList.size());
                notifyItemRemoved(position);
            }
       // }
    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//    }

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
        isLoadingAdded = false;
        shopList.clear();
        notifyDataSetChanged();
    }

    public void addSelectedShops(List<Shop> shopList) {
        Log.d("mLog", "ShopListSize: " + shopList.size());
        int previousShopListSize = this.shopList.size();
        this.shopList.addAll(shopList);
        notifyDataSetChanged();
        int amountDifference = this.shopList.size() - shopList.size();
        if(amountDifference > 0 && previousShopListSize != this.shopList.size())
            shopItemListener.scrollTo(amountDifference - 1);
    }

    public boolean getLoadingFooterState() {
        return isLoadingAdded;
    }

    public void setShopItemListener(ShopItemListener shopItemListener)
    {
        this.shopItemListener = shopItemListener;
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

        @BindView(R.id.shopItemLayout)
        ViewGroup shopItemLayout;

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public interface ShopItemListener{
        void shopSelected(String shopId);
        void scrollToBottom();

        void scrollTo(int position);
    }
}
