package apobooking.apobooking.com.secondhands.util;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.SecondHandApplication;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopRepository;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopKeyItemDataSource extends ItemKeyedDataSource<String, Shop> {

    @Inject
    ShopRepository shopRepository;

    public ShopKeyItemDataSource() {
        SecondHandApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Shop> callback) {
       shopRepository.getSelectedShops(ShopRequest.cityId, ShopRequest.shopNameId, ShopRequest.updateDayId, callback,
              "");
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Shop> callback) {
        Flowable.just(1)
                .map(__ -> {
                    shopRepository.getSelectedShops(ShopRequest.cityId, ShopRequest.shopNameId, ShopRequest.updateDayId, callback,
                            params.key);
                    return 1;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Shop> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Shop item) {
        return item.getId();
    }
}
