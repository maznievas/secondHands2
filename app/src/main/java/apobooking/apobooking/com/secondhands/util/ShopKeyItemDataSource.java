package apobooking.apobooking.com.secondhands.util;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.SecondHandApplication;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopRepository;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesFragment;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShopKeyItemDataSource extends ItemKeyedDataSource<String, Shop> {

    @Inject
    ShopRepository shopRepository;
    private LoadingStateInterface loadingStateListener;

    public ShopKeyItemDataSource() {
        SecondHandApplication.getAppComponent().inject(this);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Shop> callback) {

        Flowable.just(1)
              //  .observeOn(AndroidSchedulers.mainThread())
                .map(__ -> {
                    loadingStateListener.showLoadingView();
                    return 1;
                })
                .observeOn(Schedulers.io())
                .flatMapCompletable(__ -> {
                 return Completable.fromAction(() -> shopRepository.getSelectedShops(ShopRequest.cityId,
                         ShopRequest.shopNameId, ShopRequest.updateDayId, callback, ""));
                })
                .toFlowable()
                //.delay(1200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() ->  loadingStateListener.hideLoadingView())
                .subscribe(__ -> {
                    Log.d("mLog", "TEST1 subscribe");
                }, throwable -> {
                    Log.e("mLog", "initial loading");
                });
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

    public void setLoadingStateListener(LoadingStateInterface loadingStateListener) {
        this.loadingStateListener = loadingStateListener;
    }
}
