package andrey.project.com.secondhands.di;

import javax.inject.Singleton;

import andrey.project.com.secondhands.repositories.Local;
import andrey.project.com.secondhands.repositories.Remote;
import andrey.project.com.secondhands.repositories.shop.ShopDataSource;
import andrey.project.com.secondhands.repositories.shop.local.ShopLocalDataSource;
import andrey.project.com.secondhands.repositories.shop.remote.ShopRemoteDataSource;
import dagger.Binds;
import dagger.Module;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Deprecated
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
