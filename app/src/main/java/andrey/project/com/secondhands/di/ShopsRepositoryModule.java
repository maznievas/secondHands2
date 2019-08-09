package andrey.project.com.secondhands.di;

import javax.inject.Singleton;

import andrey.project.com.secondhands.repositories.Local;
import andrey.project.com.secondhands.repositories.shopsRepository.ShopsDataSource;
import andrey.project.com.secondhands.repositories.shopsRepository.local.ShopsLocalDataSource;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class ShopsRepositoryModule {

    @Binds
    @Local
    @Singleton
    public abstract ShopsDataSource provideLocalShopDataSource(ShopsLocalDataSource shopsLocalDataSource);
}
