package apobooking.apobooking.com.secondhands.repositories.shop;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;
import apobooking.apobooking.com.secondhands.repositories.Local;
import apobooking.apobooking.com.secondhands.repositories.Remote;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public class ShopRepository implements ShopDataSource {

    private final ShopDataSource shopLocalDataSource;
    private final ShopDataSource shopRemoteDataSource;

    @Inject
    public ShopRepository(@Local ShopDataSource shopLocalDataSource,
                          @Remote ShopDataSource shopRemoteDataSource)
    {
        this.shopLocalDataSource = shopLocalDataSource;
        this.shopRemoteDataSource = shopRemoteDataSource;
    }

    @Override
    public Completable saveShops(List<Shop> shopList) {
        return null;
    }

    @Override
    public  Maybe<List<Map<String, Object>>> getAllShops() {
        return shopRemoteDataSource.getAllShops();
//                .flatMapCompletable(shopList -> {
//                    return Completable.fromAction(() -> {
//                        shopLocalDataSource.saveShops(shopList);
//                    });
//                })
//                .andThen(shopLocalDataSource.getAllShops())
//                .onErrorResumeNext(throwable -> {
//                    return shopLocalDataSource.getAllShops();
//                });
    }

    @Override
    public Maybe<List<Map<String, Object>>> getSelectedShops(String city, String shopsName, String updateDay,
                                                             boolean needToResetLastResult, boolean needLimit) {
        return shopRemoteDataSource.getSelectedShops(city, shopsName, updateDay, needToResetLastResult, needLimit);
    }

    @Override
    public void getSelectedShops(String city, String shopsName, String updateDay,
                                 @NonNull final ItemKeyedDataSource.LoadCallback<Shop> callback,
                                 String key) {
        shopRemoteDataSource.getSelectedShops(city, shopsName, updateDay, callback, key);
    }


    @Override
    public Maybe<List<Map<String,Object>>> getAllCities() {
        return shopRemoteDataSource.getAllCities();
    }

    @Override
    public Maybe<List<Map<String,Object>>> getAllShopsNAme() {
        return shopRemoteDataSource.getAllShopsNAme();
    }

    @Override
    public Maybe<List<City>> getAllCitiesEntity() {
        return shopRemoteDataSource.getAllCitiesEntity();
    }

    @Override
    public Maybe<List<ShopName>> getAllShopNameEntity() {
        return shopRemoteDataSource.getAllShopNameEntity();
    }

    @Override
    public Flowable<String> getShopNameById(String id) {
        return shopRemoteDataSource.getShopNameById(id);
    }

    @Override
    public Flowable<Shop> getShopById(String shopId) {
        return shopRemoteDataSource.getShopById(shopId);
    }
}