package apobooking.apobooking.com.secondhands.di;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        return  mFirebaseInstance.getReference();
    }
}
