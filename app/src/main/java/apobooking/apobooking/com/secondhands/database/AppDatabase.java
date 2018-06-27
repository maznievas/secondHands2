package apobooking.apobooking.com.secondhands.database;

/**
 * Created by procreationsmac on 26/06/2018.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import apobooking.apobooking.com.secondhands.entity.Shop;

@Database(entities = {Shop.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShopDao shopDao();
}
