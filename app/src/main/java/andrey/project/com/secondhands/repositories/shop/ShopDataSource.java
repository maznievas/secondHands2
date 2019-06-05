package andrey.project.com.secondhands.repositories.shop;

import java.util.List;
import java.util.Map;

import andrey.project.com.secondhands.entity.City;
import andrey.project.com.secondhands.entity.Shop;
import andrey.project.com.secondhands.entity.ShopName;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Deprecated
public interface ShopDataSource {

    Completable saveShops(List<Shop> shopList);
    Maybe<List<Map<String, Object>>> getAllShops();
    Maybe<List<Map<String,Object>>> getSelectedShops(String city, String shopsName, String updateDay, boolean needToresetLastResult,
                                                     boolean needLimit);
    Maybe<List<Map<String,Object>>> getAllCities();
    Maybe<List<Map<String,Object>>> getAllShopsNAme();
    Maybe<List<City>> getAllCitiesEntity();
    Maybe<List<ShopName>> getAllShopNameEntity();
    Flowable<String> getShopNameById(String id);
    Flowable<Shop> getShopById(String shopId);
}
