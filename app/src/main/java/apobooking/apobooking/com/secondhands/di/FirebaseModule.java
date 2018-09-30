package apobooking.apobooking.com.secondhands.di;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

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
    FirebaseFirestore provideFirebaseFirestoreReference(FirebaseFirestoreSettings firebaseFirestoreSettings)
    {
        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        instance.setFirestoreSettings(firebaseFirestoreSettings);
        return instance;
    }

    @Provides
    @Singleton
    FirebaseStorage provideFirebaseStorage()
    {
        return FirebaseStorage.getInstance();
    }

    @Provides
    @Singleton
    FirebaseFirestoreSettings provideFirebaseFiresetoreSettings(){
        return new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
    }
}
