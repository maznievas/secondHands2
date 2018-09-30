package apobooking.apobooking.com.secondhands.util;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopsAdapterNew extends PagedListAdapter<Shop, ShopsAdapterNew.ViewHolder> {

    Context context;

    public ShopsAdapterNew(@NonNull DiffUtil.ItemCallback<Shop> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ShopsAdapterNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.shop_item, parent, false);
        return new ShopsAdapterNew.ViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsAdapterNew.ViewHolder holder, int position) {

        Shop shop = getItem(position);

        holder.shopNameTextView.setText(shop.getName());
        holder.shopAdressTextView.setText(shop.getAddress());
        holder.updateDayTextView.setText(context.getString(DayDetectHelper.detectDay(shop.getUpdateDay())));

        holder.shopItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // shopItemListener.shopSelected(shop.getId());
            }
        });

        int errorImage = R.color.recytclerViewItemColor;
        switch (shop.getName()) {
            case Const.ShopsName.ECONOM_CLASS:
                errorImage = R.drawable.econom_logo;
                break;
        }

        int finalErrorImage = errorImage;
//        Glide.with(context)
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

        //   if(position == shopList.size() - 1)
        //       SearchPropertiesFragment.allowToSearch = true;
    }

    public void setContext(Context context) {
        this.context = context;
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
}
