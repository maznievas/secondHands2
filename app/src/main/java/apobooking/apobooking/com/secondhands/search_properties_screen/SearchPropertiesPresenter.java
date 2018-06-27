package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.SecondHandApplication;
import apobooking.apobooking.com.secondhands.database.AppDatabase;
import apobooking.apobooking.com.secondhands.database.ShopDao;
import apobooking.apobooking.com.secondhands.entity.Shop;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SearchPropertiesPresenter extends MvpPresenter<SearchPropertiesView>{

    @Inject
    DatabaseReference databaseReference;

    @Inject
    AppDatabase appDatabase;

    CompositeDisposable compositeDisposable;
    private ShopDao shopDao;

    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<Shop> shopList = new ArrayList();
            for(DataSnapshot data : dataSnapshot.getChildren())
            {
                Shop shop  = data.getValue(Shop.class);
                shopList.add(shop);
                //Log.d("mLog", shop.getName());
            }
            updateShopList(shopList);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("mLog", "loadPost:onCancelled" +  databaseError.toException());
        }
    };

    public SearchPropertiesPresenter()
    {
        SecondHandApplication.getAppComponent().inject(this);
        init();
    }

    private void updateShopList(List<Shop> shopList) {
        compositeDisposable.add(
                Completable.fromAction(shopDao.insertList(shopList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                }, throwable -> {
                    throwable.printStackTrace();
                })
        );
    }

    public void init()
    {
        shopDao = appDatabase.shopDao();
        databaseReference.addValueEventListener(postListener);

        //todo: uncomment for adding items to firebase
//        for(int i = 0; i < 1; i++) {
//            String id = databaseReference.push().getKey();
//            Shop shop = new Shop();
//            shop.setId(id);
//            shop.setName("Econom class");
//            shop.setAddress("Test address");
//            shop.setUpdateDay(1);
//
//            databaseReference.child(String.valueOf(shop.getId())).setValue(shop);
//        }
    }

    public void clear()
    {}
}
//Hierarchical transitions
