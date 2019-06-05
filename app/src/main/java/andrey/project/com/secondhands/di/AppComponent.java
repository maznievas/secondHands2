package andrey.project.com.secondhands.di;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import andrey.project.com.secondhands.database.AppDatabase;
import andrey.project.com.secondhands.mainFragment.MainPresenter;
import andrey.project.com.secondhands.map_screen.MapPresenter;
import andrey.project.com.secondhands.repositories.shop.ShopRepository;
import andrey.project.com.secondhands.search_properties_screen.SearchPropertiesPresenter;
import andrey.project.com.secondhands.util.ImageFragment;
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
    void inject(ImageFragment imageFragment);
    void inject(MainPresenter mainPresenter);
}