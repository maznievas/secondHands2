package apobooking.apobooking.com.secondhands.repositories.shop;

import com.google.firebase.database.DataSnapshot;

import java.util.List;
import java.util.Map;

import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public interface ShopDataSource {

    Completable saveShops(List<Shop> shopList);
    Maybe<List<Map<String, Object>>> getAllShops();
    Maybe<List<Map<String,Object>>> getSelectedShops(String city, String shopsName, String updateDay);
    Maybe<List<Map<String,Object>>> getAllCities();
    Maybe<List<Map<String,Object>>> getAllShopsNAme();
    Maybe<List<City>> getAllCitiesEntity();
    Maybe<List<ShopName>> getAllShopNameEntity();
    Flowable<String> getShopNameById(String id);
}
