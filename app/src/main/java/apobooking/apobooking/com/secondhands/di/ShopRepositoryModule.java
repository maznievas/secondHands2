package apobooking.apobooking.com.secondhands.di;

import javax.inject.Singleton;

import apobooking.apobooking.com.secondhands.repositories.Local;
import apobooking.apobooking.com.secondhands.repositories.Remote;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopDataSource;
import apobooking.apobooking.com.secondhands.repositories.shop.local.ShopLocalDataSource;
import apobooking.apobooking.com.secondhands.repositories.shop.remote.ShopRemoteDataSource;
import dagger.Binds;
import dagger.Module;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Module
public abstract class ShopRepositoryModule {

    @Binds
    @Local
    @Singleton
    public abstract ShopDataSource provideLocalShopDataSource(ShopLocalDataSource shopLocalDataSource);

    @Binds
    @Remote
    @Singleton
    public abstract ShopDataSource provideRemoteShopDataSource(ShopRemoteDataSource shopRemoteDataSource);
}
