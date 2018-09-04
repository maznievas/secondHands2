package apobooking.apobooking.com.secondhands.repositories.shop.remote;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopDataSource;
import apobooking.apobooking.com.secondhands.util.Const;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public class ShopRemoteDataSource implements ShopDataSource {

    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastResult;
    //private int currentPage = 0;

    @Inject
    public ShopRemoteDataSource(DatabaseReference databaseReference,
                                FirebaseFirestore firebaseFirestore) {
        this.databaseReference = databaseReference;
        this.firebaseFirestore = firebaseFirestore;
        // this.firebaseFirestore.setLoggingEnabled(true);
    }

    @Override
    public Completable saveShops(List<Shop> shopList) {
        return null;
    }


    @Override
    public Maybe<List<Map<String, Object>>> getAllShops() {
        return Maybe.create(new MaybeOnSubscribe<List<Map<String, Object>>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Map<String, Object>>> e) throws Exception {
                firebaseFirestore.collection(Const.Firebase.Tables.SHOPS)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Map<String, Object>> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(document.getData());
                                        // Log.d("mLog", document.getId() + " => " + document.getData());
                                    }
                                    e.onSuccess(list);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting all shops.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<List<Map<String, Object>>> getSelectedShops(String city, String shopsName, String updateDay,
                                                             boolean needToResetLastResult, boolean needLimit) {
        Query collection;
        if (needLimit)
            collection = firebaseFirestore.collection(Const.Firebase.Tables.SHOPS)
                    // .orderBy(Const.Firebase.UPDATE_DAY)
                    .limit(Const.RecyclerView.TOTAL_ITEM_EACH_LOAD);
        else
            collection = firebaseFirestore.collection(Const.Firebase.Tables.SHOPS);

        if (!updateDay.equals("7"))
            collection = collection.whereEqualTo(Const.Firebase.UPDATE_DAY,
                    Integer.parseInt(updateDay));
        if (!TextUtils.isEmpty(city)) {
            collection = collection.whereEqualTo(Const.Firebase.CITY_ID, city);
        }
        if (!TextUtils.isEmpty(shopsName)) {
            collection = collection.whereEqualTo(Const.Firebase.NAME_ID, shopsName);
        }

        Log.d("mLog", "Detected city: " + city);
        Log.d("mLog", "Detected name: " + shopsName);

        if (needToResetLastResult)
            lastResult = null;

        if (lastResult != null)
            collection = collection.startAfter(lastResult);
        Query finalCollection = collection;

        return Maybe.create(emitter -> finalCollection
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Map<String, Object>> list = new ArrayList<>();
                    if (lastResult != null && queryDocumentSnapshots.size() > 0) {
                        if (!lastResult.equals(queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() - 1))) {
                            for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                                Map<String, Object> map = querySnapshot.getData();
                                map.put(Const.Firebase.SHOP_ID, querySnapshot.getId());
                                list.add(map);
                            }
                        } else
                            return;
                    } else
                        for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                            Map<String, Object> map = querySnapshot.getData();
                            map.put(Const.Firebase.SHOP_ID, querySnapshot.getId());
                            list.add(map);
                        }

                    if (queryDocumentSnapshots.size() > 0) {
//                        if(lastResult != null) {
//                            if(!lastResult.equals(queryDocumentSnapshots.getDocuments()))
//                                lastResult = queryDocumentSnapshots.getDocuments()
//                                    .get(queryDocumentSnapshots.size() - 1);
//                            else
//                                lastResult = null;
//                        }
//                        else
                        lastResult = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() - 1);
                        Log.d("DocumentSnapShot", "id: " + lastResult.getId());
                    }

                    emitter.onSuccess(list);
                    emitter.onComplete();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("mLog", "Error getting shops");
                        e.printStackTrace();
                    }
                }));
        // }
    }


    @Override
    public Maybe<List<Map<String, Object>>> getAllCities() {
        return Maybe.create(new MaybeOnSubscribe<List<Map<String, Object>>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Map<String, Object>>> e) throws Exception {
                firebaseFirestore.collection(Const.Firebase.Tables.CITIES)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Map<String, Object>> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(document.getData());
                                        // Log.d("mLog", document.getId() + " => " + document.getData());
                                    }
                                    e.onSuccess(list);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<List<Map<String, Object>>> getAllShopsNAme() {
        return Maybe.create(new MaybeOnSubscribe<List<Map<String, Object>>>() {
            @Override
            public void subscribe(MaybeEmitter<List<Map<String, Object>>> e) throws Exception {
                firebaseFirestore.collection(Const.Firebase.Tables.SHOPS_NAME)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Map<String, Object>> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(document.getData());
                                        // Log.d("mLog", document.getId() + " => " + document.getData());
                                    }
                                    e.onSuccess(list);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<List<City>> getAllCitiesEntity() {
        return Maybe.create(new MaybeOnSubscribe<List<City>>() {
            @Override
            public void subscribe(MaybeEmitter<List<City>> e) throws Exception {
                firebaseFirestore.collection(Const.Firebase.Tables.CITIES)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<City> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        City city = new City();
                                        city.setId(document.getId());
                                        city.setName(document.getData().get("name").toString());
                                        list.add(city);
                                        // Log.d("mLog", document.getId() + " => " + document.getData());
                                    }
                                    e.onSuccess(list);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<List<ShopName>> getAllShopNameEntity() {
        return Maybe.create(new MaybeOnSubscribe<List<ShopName>>() {
            @Override
            public void subscribe(MaybeEmitter<List<ShopName>> e) throws Exception {
                firebaseFirestore.collection(Const.Firebase.Tables.SHOPS_NAME)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("mLog", "Check time");
                                    List<ShopName> list = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        ShopName shopName = new ShopName();
                                        shopName.setId(document.getId());
                                        shopName.setName(document.getData().get("name").toString());
                                        list.add(shopName);
                                        // Log.d("mLog", document.getId() + " => " + document.getData());
                                    }
                                    e.onSuccess(list);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    @Override
    public Flowable<String> getShopNameById(String id) {
        CollectionReference collection = firebaseFirestore.collection(Const.Firebase.Tables.SHOPS_NAME);
        DocumentReference document = collection.document(id);

        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                document.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String name
                                            // for (DocumentSnapshot document : task.getResult()) {
                                            = task.getResult().getData().get("name").toString();
                                    Log.d("mLog", "Name:: " + task.getResult().getId() +
                                            " => " + task.getResult().getData());
                                    // }
                                    e.onNext(name);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting shopNames", task.getException());
                                }
                            }
                        });
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Shop> getShopById(String shopId) {
        CollectionReference collection = firebaseFirestore.collection(Const.Firebase.Tables.SHOPS);
        DocumentReference document = collection.document(shopId);

        return Flowable.create(new FlowableOnSubscribe<Shop>() {
            @Override
            public void subscribe(FlowableEmitter<Shop> e) throws Exception {
                document.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Shop shop = new Shop();
                                    Map<String, Object> map = task.getResult().getData();
                                    shop.setId(task.getResult().getId());
                                    shop.setImageName(map.get(Const.Firebase.IMAGE_PATH).toString());
                                    shop.setCityId(map.get(Const.Firebase.CITY_ID).toString());
                                    shop.setNameId(map.get(Const.Firebase.NAME_ID).toString());
                                    shop.setUpdateDay(Integer
                                            .parseInt(map.get(Const.Firebase.UPDATE_DAY).toString()));
                                    shop.setAddress(map.get(Const.Firebase.ADDRESS).toString());
                                    shop.setImages((ArrayList<String>) map.get(Const.Firebase.IMAGES_ARRAY));

                                    e.onNext(shop);
                                    e.onComplete();
                                } else {
                                    Log.w("mLog", "Error getting single shop", task.getException());
                                }
                            }
                        });
            }
        }, BackpressureStrategy.BUFFER);
    }
}