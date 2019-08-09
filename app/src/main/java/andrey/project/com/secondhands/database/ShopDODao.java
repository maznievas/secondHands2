package andrey.project.com.secondhands.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import andrey.project.com.secondhands.api.dataObjects.ShopDO;

@Dao
public interface ShopDODao {

    @Insert
    void insertShops(List<ShopDO> shopDOList);

    @Query("SELECT * FROM ShopDO")
    List<ShopDO> getShops();

    @Query("DELETE FROM ShopDO")
    void deleteShops();
}
