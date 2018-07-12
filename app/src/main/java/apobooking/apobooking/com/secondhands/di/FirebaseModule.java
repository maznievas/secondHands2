package apobooking.apobooking.com.secondhands.di;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {
    private FirebaseDatabase mFirebaseInstance;

    @Provides
    @Singleton
    DatabaseReference provideFirebaseReference()
    {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.setPersistenceEnabled(true);
        return  mFirebaseInstance.getReference();
    }

    @Provides
    @Singleton
    FirebaseFirestore provideFirebaseFirestoreReference()
    {
        return FirebaseFirestore.getInstance();
    }
}
