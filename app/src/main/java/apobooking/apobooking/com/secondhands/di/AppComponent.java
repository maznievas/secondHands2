package apobooking.apobooking.com.secondhands.di;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import apobooking.apobooking.com.secondhands.database.AppDatabase;
import apobooking.apobooking.com.secondhands.map_screen.MapPresenter;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopRepository;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesPresenter;
import dagger.Component;

@Singleton
@Component(modules =
        {
                FirebaseModule.class,
                DatabaseModule.class,
                ShopRepositoryModule.class
        })
public interface AppComponent {
    DatabaseReference databaseReference();
    FirebaseFirestore firebaseFireStore();
    FirebaseStorage firebaseStorage();
    AppDatabase appDataBase();
    ShopRepository shopRepository();

    void inject(SearchPropertiesPresenter searchPropertiesPresenter);
    void inject(MapPresenter mapPresenter);
}