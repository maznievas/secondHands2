package apobooking.apobooking.com.secondhands.di;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Singleton;

import apobooking.apobooking.com.secondhands.database.AppDatabase;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesPresenter;
import dagger.Component;

@Singleton
@Component(modules =
        {
                FirebaseModule.class,
                DatabaseModule.class
        })
public interface AppComponent {
    DatabaseReference databaseReference();
    AppDatabase appDataBase();

    void inject(SearchPropertiesPresenter searchPropertiesPresenter);
}