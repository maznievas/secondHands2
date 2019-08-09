package andrey.project.com.secondhands.database;

/**
 * Created by procreationsmac on 26/06/2018.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import andrey.project.com.secondhands.api.dataObjects.ShopDO;
import andrey.project.com.secondhands.api.dataObjects.ShopName;
import andrey.project.com.secondhands.api.dataObjects.TownDO;
import andrey.project.com.secondhands.entity.Shop;

@Database(entities = {Shop.class, ShopDO.class, ShopName.class, TownDO.class}, version = 7,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShopDao shopDao();

    public abstract ShopDODao shopDODao();
    public abstract ShopNameDao shopNameDao();
    public abstract TownDODao townDODao();
}
