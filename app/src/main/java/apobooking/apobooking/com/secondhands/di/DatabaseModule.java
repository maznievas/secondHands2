package apobooking.apobooking.com.secondhands.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import apobooking.apobooking.com.secondhands.database.AppDatabase;
import dagger.Module;
import dagger.Provides;

/**
 * Created by procreationsmac on 26/06/2018.
 */

@Module
public class DatabaseModule {

    Context context;

    public DatabaseModule(Context context)
    {
        this.context = context;
    }

    @Provides
    @Singleton
    AppDatabase provideDatabse()
    {
        return Room.databaseBuilder(context, AppDatabase.class, "Shops.db")
                .fallbackToDestructiveMigration()
                .build();
    }
}
