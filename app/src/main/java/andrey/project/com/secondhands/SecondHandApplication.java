package andrey.project.com.secondhands;

import android.support.multidex.MultiDexApplication;

import andrey.project.com.secondhands.di.AppComponent;
import andrey.project.com.secondhands.di.DaggerAppComponent;
import andrey.project.com.secondhands.di.DatabaseModule;

/**
 * Created by procreationsmac on 26/06/2018.
 */

public class SecondHandApplication extends MultiDexApplication {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent()
    {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .build();
    }
}
