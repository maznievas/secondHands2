package andrey.project.com.secondhands.repositories.shop.local;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import andrey.project.com.secondhands.database.AppDatabase;
import andrey.project.com.secondhands.database.ShopDao;
import andrey.project.com.secondhands.entity.City;
import andrey.project.com.secondhands.entity.Shop;
import andrey.project.com.secondhands.entity.ShopName;
import andrey.project.com.secondhands.repositories.shop.ShopDataSource;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Deprecated
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
    public Maybe<List<Map<String, Object>>> getSelectedShops(String city, String shopsName, String updateDay,
                                                             boolean needToResetLastResult, boolean needLimit) {
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

    @Override
    public Flowable<Shop> getShopById(String shopId) {
        return null;
    }
}
