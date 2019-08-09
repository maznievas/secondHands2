package andrey.project.com.secondhands.di;

import javax.inject.Singleton;

import andrey.project.com.secondhands.repositories.Local;
import andrey.project.com.secondhands.repositories.helpfulRepository.HelpfulDataSource;
import andrey.project.com.secondhands.repositories.helpfulRepository.HelpfulRepository;
import andrey.project.com.secondhands.repositories.helpfulRepository.local.HelpfulLocalDataSource;
import andrey.project.com.secondhands.repositories.shopsRepository.ShopsDataSource;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class HelpfulRepositoryModule {
    @Binds
    @Local
    @Singleton
    public abstract HelpfulDataSource provideLocalHelpfulDataSource(HelpfulLocalDataSource helpfulLocalDataSource);
}
