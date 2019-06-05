package andrey.project.com.secondhands.database;

/**
 * Created by procreationsmac on 26/06/2018.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import andrey.project.com.secondhands.entity.Shop;

@Database(entities = {Shop.class}, version = 6,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShopDao shopDao();
}
