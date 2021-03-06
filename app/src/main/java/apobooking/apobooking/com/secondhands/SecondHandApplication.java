package apobooking.apobooking.com.secondhands;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import apobooking.apobooking.com.secondhands.di.AppComponent;
import apobooking.apobooking.com.secondhands.di.DaggerAppComponent;
import apobooking.apobooking.com.secondhands.di.DatabaseModule;

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
