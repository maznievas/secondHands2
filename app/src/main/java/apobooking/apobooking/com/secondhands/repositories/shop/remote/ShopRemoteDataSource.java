package apobooking.apobooking.com.secondhands.repositories.shop.remote;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public Maybe<List<Map<String, Object>>> getSelectedShops(String city, String shopsName, String updateDay) {
        CollectionReference collection = firebaseFirestore.collection(Const.Firebase.Tables.SHOPS);
        Task taskCity = null;
        Task taskUpdateDay = null;
        Task taskName = null;

        if (!updateDay.equals("7"))
            taskUpdateDay = collection.whereEqualTo(Const.Firebase.UPDATE_DAY,
                    Integer.parseInt(updateDay)).get();
        if (!TextUtils.isEmpty(city))
            taskCity = collection.whereEqualTo(Const.Firebase.CITY_ID, city).get();
        if (!TextUtils.isEmpty(shopsName))
            taskName = collection.whereEqualTo(Const.Firebase.NAME_ID, shopsName).get();

        Log.d("mLog", "Detected city: " + city);
        Log.d("mLog", "Detected name: " + shopsName);

        Task finalTaskCity = taskCity;
        Task finalTaskUpdateDay = taskUpdateDay;
        Task finalTaskName = taskName;

        int taskSize = 0;
        if (finalTaskCity != null)
            taskSize++;
        if (finalTaskName != null)
            taskSize++;
        if (finalTaskUpdateDay != null)
            taskSize++;

        int iterator = -1;
        Task[] tasksArray = new Task[taskSize];
        if (finalTaskCity != null)
            tasksArray[++iterator] = finalTaskCity;
        if (finalTaskName != null)
            tasksArray[++iterator] = finalTaskName;
        if (finalTaskUpdateDay != null)
            tasksArray[++iterator] = finalTaskUpdateDay;

        if (taskSize == 0)
            return getAllShops();
        else
            return Maybe.create(new MaybeOnSubscribe<List<Map<String, Object>>>() {
                @Override
                public void subscribe(MaybeEmitter<List<Map<String, Object>>> e) throws Exception {

                    Tasks.whenAllSuccess(tasksArray)
                            .addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> objects) {
                                    Log.d("mLog", "Success");
                                    List<Map<String, Object>> generalList = new ArrayList<>();
                                    boolean firstTime = true;
                                    for (Object object : objects) {
                                        Log.d("mLog", "Object size: " + objects.size());
                                        List<Map<String, Object>> tempList = new ArrayList<>();
                                        for (DocumentSnapshot document : ((QuerySnapshot) object).getDocuments()) {
                                            tempList.add(document.getData());
                                            Log.d("mLog", "DATA from firestore: " + document.getId() + " => " + document.getData());
                                        }
                                        if (firstTime && tempList.size() > 0) {
                                            generalList.addAll(tempList);
                                            firstTime = false;
                                        }
                                        if (tempList.size() > 0) {
                                            generalList.retainAll(tempList);
                                        }
                                        Log.d("mLog", "New Document snapshot");
                                    }
                                    e.onSuccess(generalList);
                                    e.onComplete();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            });
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
}