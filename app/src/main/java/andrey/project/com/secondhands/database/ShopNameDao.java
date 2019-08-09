package andrey.project.com.secondhands.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import andrey.project.com.secondhands.api.dataObjects.ShopName;

@Dao
public interface ShopNameDao {

    @Insert
    void insertShopNames(List<ShopName> shopNames);

    @Query("SELECT * FROM ShopName")
    List<ShopName> getShopNames();

    @Query("DELETE FROM ShopName")
    void deleteShopNames();
}
