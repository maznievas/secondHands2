package apobooking.apobooking.com.secondhands.repositories.shop.local;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.database.AppDatabase;
import apobooking.apobooking.com.secondhands.database.ShopDao;
import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopDataSource;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public class ShopLocalDataSource implements ShopDataSource {

    private ShopDao shopDao;

    @Inject
    public ShopLocalDataSource(AppDatabase appDatabase) {
        shopDao = appDatabase.shopDao();
    }

    @Override
    public Completable saveShops(List<Shop> shopList) {
        return Completable.fromAction(() -> {
            shopDao.insertList(shopList);
        });
              //  .subscribeOn(Schedulers.io());
    }

    @Override
    public Maybe<List<Map<String, Object>>> getAllShops() {
    return null;
    }

    @Override
    public Maybe<List<Map<String, Object>>> getSelectedShops(String city, String shopsName, String updateDay) {
        return null;
    }


    @Override
    public Maybe<List<Map<String, Object>>> getAllCities() {
        return null;
    }

    @Override
    public Maybe<List<Map<String,Object>>> getAllShopsNAme() {
        return null;
    }

    @Override
    public Maybe<List<City>> getAllCitiesEntity() {
        return null;
    }

    @Override
    public Maybe<List<ShopName>> getAllShopNameEntity() {
        return null;
    }

    @Override
    public Flowable<String> getShopNameById(String id) {
        return null;
    }
}
