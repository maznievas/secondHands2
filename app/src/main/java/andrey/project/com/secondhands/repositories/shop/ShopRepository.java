package andrey.project.com.secondhands.repositories.shop;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import andrey.project.com.secondhands.entity.City;
import andrey.project.com.secondhands.entity.Shop;
import andrey.project.com.secondhands.entity.ShopName;
import andrey.project.com.secondhands.repositories.Local;
import andrey.project.com.secondhands.repositories.Remote;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Deprecated
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