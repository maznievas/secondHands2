package andrey.project.com.secondhands.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import andrey.project.com.secondhands.entity.Shop;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Deprecated
@Dao
public interface ShopDao {
    @Query("Select *  FROM Shop")
    Maybe<List<Shop>> getAllShops();

    @Query("Select * FROM Shop WHERE id=:shopId LIMIT 1")
    Single<Shop> selectShopById(int shopId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Shop> shopList);
}
