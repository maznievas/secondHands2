package apobooking.apobooking.com.secondhands.repositories.shop.remote;

import android.arch.paging.ItemKeyedDataSource;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static apobooking.apobooking.com.secondhands.util.Const.Firebase.ADDRESS;

/**
 * Created by procreationsmac on 09/07/2018.
 */

public class ShopRemoteDataSource implements ShopDataSource {

    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastResult;
    private DocumentSnapshot lastDocumentSnapShot;
    //private int currentPage = 0;
    private StorageReference gsReference;

    @Inject
    FirebaseStorage firebaseStorage;

    @Inject
    public ShopRemoteDataSource(DatabaseReference databaseReference,
                                FirebaseFirestore firebaseFirestore) {
        this.databaseReference = databaseReference;
        this.firebaseFirestore = firebaseFirestore;
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

        Log.d("mLog", "Detected cityId: " + city);
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
    public void getSelectedShops(String city, String shopsName,
                                 String updateDay,
                                 @NonNull final ItemKeyedDataSource.LoadCallback<Shop> callback,
                                 String key) {
        Query collection;
        collection = firebaseFirestore.collection(Const.Firebase.Tables.SHOPS)
                .limit(Const.RecyclerView.TOTAL_ITEM_EACH_LOAD);

        if (!updateDay.equals("7") && !TextUtils.isEmpty(updateDay))
            collection = collection.whereEqualTo(Const.Firebase.UPDATE_DAY,
                    Integer.parseInt(updateDay));
        if (!TextUtils.isEmpty(city)) {
            collection = collection.whereEqualTo(Const.Firebase.CITY_ID, city);
        }
        if (!TextUtils.isEmpty(shopsName)) {
            collection = collection.whereEqualTo(Const.Firebase.NAME_ID, shopsName);
        }

        Log.d("mLog", "Detected cityId: " + city);
        Log.d("mLog", "Detected name: " + shopsName);
        Log.d("mLog", "Detected updateDay: " + updateDay);

        OnSuccessListener<QuerySnapshot> onSuccessListener = new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Shop> shopList = new ArrayList<>();

                Log.d("mLog", "queryDocumentSnapshots.getDocuments().size: " + queryDocumentSnapshots.getDocuments().size());

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Shop shop = documentSnapshot.toObject(Shop.class);
                    shop.setId(documentSnapshot.getId());
                    shopList.add(shop);

                    Log.d("mLog", "REF: " + Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImagePath());
                    gsReference = firebaseStorage
                            .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImagePath());
                    shop.setImageReference(gsReference);

                    List<StorageReference> storageList = new ArrayList<>();
                    for(String imagePath : shop.getImages())
                    {
                        gsReference = firebaseStorage
                                .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + imagePath);
                        storageList.add(gsReference);
                    }
                    shop.setImagesReference(storageList);

                    lastDocumentSnapShot = documentSnapshot;
                }

                if (shopList.size() == 0)
                    return;

                Flowable.fromIterable(shopList)
                        .flatMap(shop -> {
                            return getShopNameById(shop.getNameId())
                                    .map(nameOfShop -> {
                                        shop.setName(nameOfShop);
                                        return shop;
                                    });
                        })
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(__ -> {
                            Log.d("mLog", "Detecting shop's name.Subscribe");
                            if (callback instanceof ItemKeyedDataSource.LoadInitialCallback) {
                                //initial load
                                ((ItemKeyedDataSource.LoadInitialCallback) callback)
                                        .onResult(shopList, 0, shopList.size());
                            } else {
                                //next pages load
                                callback.onResult(shopList);
                            }
                        }, throwable -> {
                            Log.e("mLog", "Get all shops' name");
                            throwable.printStackTrace();
                        });
            }
        };

        if (!TextUtils.isEmpty(key)) {

            collection = collection
                    .startAfter(lastDocumentSnapShot);//use only here because if I have key - have not null lastDocumentSnapShot

            collection.get().addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(e -> {
                        Log.e("mLog", "Selecting shops from firestore");
                        e.printStackTrace();
                    });
        } else
            collection.get().addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("mLog", "Selecting shops from firestore");
                            e.printStackTrace();
                        }
                    });

       // Query finalCollection = collection;


//        finalCollection
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.e("mLog", "exception in fetching from firestore", e);
//                            return;
//                        }
//                        List<Shop> shopList = new ArrayList<>();
//
//                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
//                            Shop shop = documentSnapshot.toObject(Shop.class);
//                            shop.setId(documentSnapshot.getId());
//                            shopList.add(shop);
//                        }
//
//                        if (shopList.size() == 0)
//                            return;
//
//                        Flowable.fromIterable(shopList)
//                                .flatMap(shop -> {
//                                    return getShopNameById(shop.getNameId())
//                                            .map(nameOfShop -> {
//                                                shop.setName(nameOfShop);
//                                                return shop;
//                                            });
//                                })
//                                .toList()
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(__ -> {
//                                    Log.d("mLog", "Detecting shop's name.Subscribe");
//                                    if (callback instanceof ItemKeyedDataSource.LoadInitialCallback) {
//                                        //initial load
//                                        ((ItemKeyedDataSource.LoadInitialCallback) callback)
//                                                .onResult(shopList, 0, shopList.size());
//                                    } else {
//                                        //next pages load
//                                        callback.onResult(shopList);
//                                    }
//                                }, throwable -> {
//                                    Log.e("mLog", "Get all shops' name");
//                                    throwable.printStackTrace();
//                                });
//                    }
//                });

//        return Maybe.create(emitter -> finalCollection
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<Map<String, Object>> list = new ArrayList<>();
//                    if (lastResult != null && queryDocumentSnapshots.size() > 0) {
//                        if (!lastResult.equals(queryDocumentSnapshots.getDocuments()
//                                .get(queryDocumentSnapshots.size() - 1))) {
//                            for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
//                                Map<String, Object> map = querySnapshot.getData();
//                                map.put(Const.Firebase.SHOP_ID, querySnapshot.getId());
//                                list.add(map);
//                            }
//                        } else
//                            return;
//                    } else
//                        for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
//                            Map<String, Object> map = querySnapshot.getData();
//                            map.put(Const.Firebase.SHOP_ID, querySnapshot.getId());
//                            list.add(map);
//                        }
//
//                    if (queryDocumentSnapshots.size() > 0) {
//                        lastResult = queryDocumentSnapshots.getDocuments()
//                                .get(queryDocumentSnapshots.size() - 1);
//                        Log.d("DocumentSnapShot", "id: " + lastResult.getId());
//                    }
//
//                    emitter.onSuccess(list);
//                    emitter.onComplete();
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("mLog", "Error getting shops");
//                        e.printStackTrace();
//                    }
//                })
//        );
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
                                    shop.setImagePath(map.get(Const.Firebase.IMAGE_PATH).toString());
                                    shop.setCityId(map.get(Const.Firebase.CITY_ID).toString());
                                    shop.setNameId(map.get(Const.Firebase.NAME_ID).toString());
                                    shop.setUpdateDay(Integer
                                            .parseInt(map.get(Const.Firebase.UPDATE_DAY).toString()));
                                    shop.setAddress(map.get(ADDRESS).toString());
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

//    public interface TaskLifecycle{
//        void executed(DocumentSnapshot documentSnapshot);
//    }
}