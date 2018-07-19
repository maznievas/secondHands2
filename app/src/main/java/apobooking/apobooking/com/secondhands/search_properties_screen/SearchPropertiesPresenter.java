package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.SecondHandApplication;
import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopRepository;
import apobooking.apobooking.com.secondhands.util.Const;
import apobooking.apobooking.com.secondhands.util.DayDetectHelper;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SearchPropertiesPresenter extends MvpPresenter<SearchPropertiesView> {

    @Inject
    DatabaseReference databaseReference;

    @Inject
    FirebaseFirestore firebaseFirestore;

    @Inject
    ShopRepository shopRepository;

    CompositeDisposable compositeDisposable;

//    ValueEventListener postListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            List<Shop> shopList = new ArrayList();
//            List<String> citiesList = new ArrayList();
//            List<String> shopsNameList = new ArrayList<>();
//
//            for (DataSnapshot data : dataSnapshot.child("shops").getChildren()) {
//                Shop shop = data.getValue(Shop.class);
//                shopList.add(shop);
//                //  Log.d("mLog", shop.getName());
//            }
//            updateShopList(shopList);
//
//            for (DataSnapshot data : dataSnapshot.child("cities").getChildren()) {
//                String city = data.getValue().toString();
//                citiesList.add(city);
//            }
//            for (DataSnapshot data : dataSnapshot.child("shopsName").getChildren()) {
//                String shopNmae = data.getValue().toString();
//                shopsNameList.add(shopNmae);
//            }
//            citiesList.add(0, Const.Firebase.ALL_CITIES);
//            shopsNameList.add(0, Const.Firebase.ALL_SHOPS);
//            // getViewState().setSpinnerData(citiesList, shopsNameList);
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            // Getting Post failed, log a message
//            Log.w("mLog", "loadPost:onCancelled" + databaseError.toException());
//        }
//    };

    public SearchPropertiesPresenter() {
        SecondHandApplication.getAppComponent().inject(this);
        init();
    }

    private void updateShopList(List<Shop> shopList) {
//        compositeDisposable.add(
//                Completable.fromAction(() -> shopDao.insertList(shopList))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doOnSubscribe(v -> getViewState().showLoadingState())
//                        .doOnTerminate(() -> getViewState().hideLoadingstate())
//                        .subscribe(() -> {
//                            Log.d("mLog", "size : " + shopList.size());
//                            getViewState().setSelectedShops(shopList);
//                        }, throwable -> {
//                            throwable.printStackTrace();
//                        })
//        );
    }


    public void init() {
        compositeDisposable = new CompositeDisposable();


        //    databaseReference.addListenerForSingleValueEvent(postListener);

        //todo:uncomment for adding data to firestore
//        Map<String, Object> data = new HashMap<>();
//        data.put("nameId", "7GYe0P6TrjKD6FYUHB6m");
//        data.put("address", "");
//        data.put("cityId", "qcFHZtVdGmmDGKpTGAbC");
//        data.put("updateDay", 0);
//
//        for (int i = 0; i < 19; i++)
//            firebaseFirestore.collection("shops")
//                    .add(data)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d("mLog", "DocumentSnapshot written with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("mLog", "Error adding document", e);
//                        }
//                    });


        //todo: uncomment for adding  shops' name items to firebase
//        for(int i = 0; i < 1; i++) {
//            String id = databaseReference.push().getKey();
//            databaseReference.child("shopsName").child(id).setValue("Econom class");
//        }

        //todo: uncomment for adding  cities to firebase
//        for(int i = 0; i < 1; i++) {
//            String id = databaseReference.push().getKey();
//            databaseReference.child("cities").child(id).setValue("Запорожье");
//        }

        //todo: uncomment for adding shop items to firebase
//        for(int i = 0; i < 1; i++) {
//            String id = databaseReference.push().getKey();
//            Shop shop = new Shop();
//            shop.setId(id);
//            shop.setName("Запорожье");a
//            shop.setName("Econom class");
//            shop.setAddress("Test address");
//            shop.setUpdateDay(1);
//
//            databaseReference.child("shops").child(String.valueOf(shop.getId())).setValue(shop);
//        }
    }

    public void selectShops(String city, String shopsName, String updateDay, boolean needToResetLastResult,
             boolean needLimit) {
        final String[] cityIdFinal = new String[1];
        final String[] shopsNameIdFinal = new String[1];
        compositeDisposable.add(shopRepository.getAllCitiesEntity()
                .toFlowable()
                //  .flatMapIterable(cityList -> cityList)
                .map(allCitiesList -> {
                    for (City cityMap : allCitiesList) {
                        if (cityMap.getName().equals(city))
                            cityIdFinal[0] = cityMap.getId();
                    }
                    return 1;
                })
                .flatMap(ignored -> {
                    return shopRepository.getAllShopNameEntity()
                            .toFlowable();
                })
                .map(allShopsNameList -> {
                    for (ShopName shopNameFirebase : allShopsNameList) {
                        if (shopNameFirebase.getName().equals(shopsName))
                            shopsNameIdFinal[0] = shopNameFirebase.getId();
                    }
                    return 1;
                })
                .flatMap(ignored -> {
                    return shopRepository.getSelectedShops(cityIdFinal[0], shopsNameIdFinal[0],
                            String.valueOf(DayDetectHelper.detectDay(updateDay)), needToResetLastResult,
                            needLimit)
                            .toFlowable();
                })
                .flatMapIterable(shopMapList -> shopMapList)
                .flatMap(shopMap -> {
                    return Flowable.just(new Shop())
                            .map(shop -> {
                                shop.setNameId(shopMap.get(Const.Firebase.NAME_ID).toString());
                                shop.setAddress(shopMap.get(Const.Firebase.ADDRESS).toString());
                                shop.setUpdateDay(Integer.parseInt(shopMap.get(Const.Firebase.UPDATE_DAY).toString()));
                                return shop;
                            })
                            .flatMap(shop -> {
                                return shopRepository.getShopNameById(shop.getNameId())
                                        .map(shopName -> {
                                            shop.setName(shopName);
                                            return shop;
                                        });
                            });
                })
                .toList()
                .toFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(v -> getViewState().showLoadingState())
                .doOnTerminate(() -> getViewState().hideLoadingstate())
                .subscribe(shopList -> {
                    getViewState().addSelectedShops(shopList);
                }, throwable -> {
                    Log.e("mLog", "Update shops");
                    throwable.printStackTrace();
                })
        );
    }

 //   public Flowable<List<Shop>> selectShopsFlowable(String city, String shopsName, String updateDay)
  //  {
//        final String[] cityIdFinal = new String[1];
//        final String[] shopsNameIdFinal = new String[1];
//        return shopRepository.getAllCitiesEntity()
//                .toFlowable()
//                //  .flatMapIterable(cityList -> cityList)
//                .map(allCitiesList -> {
//                    for (City cityMap : allCitiesList) {
//                        if (cityMap.getName().equals(city))
//                            cityIdFinal[0] = cityMap.getId();
//                    }
//                    return 1;
//                })
//                .flatMap(ignored -> {
//                    return shopRepository.getAllShopNameEntity()
//                            .toFlowable();
//                })
//                .map(allShopsNameList -> {
//                    for (ShopName shopNameFirebase : allShopsNameList) {
//                        if (shopNameFirebase.getName().equals(shopsName))
//                            shopsNameIdFinal[0] = shopNameFirebase.getId();
//                    }
//                    return 1;
//                })
//                .flatMap(ignored -> {
//                    return shopRepository.getSelectedShops(cityIdFinal[0], shopsNameIdFinal[0],
//                            String.valueOf(DayDetectHelper.detectDay(updateDay)))
//                            .toFlowable();
//                })
//                .flatMapIterable(shopMapList -> shopMapList)
//                .flatMap(shopMap -> {
//                    return Flowable.just(new Shop())
//                            .map(shop -> {
//                                shop.setNameId(shopMap.get(Const.Firebase.NAME_ID).toString());
//                                shop.setAddress(shopMap.get(Const.Firebase.ADDRESS).toString());
//                                shop.setUpdateDay(Integer.parseInt(shopMap.get(Const.Firebase.UPDATE_DAY).toString()));
//                                return shop;
//                            })
//                            .flatMap(shop -> {
//                                return shopRepository.getShopNameById(shop.getNameId())
//                                        .map(shopName -> {
//                                            shop.setName(shopName);
//                                            return shop;
//                                        });
//                            });
//                })
//                .toList()
//                .toFlowable()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
////                .doOnSubscribe(v -> getViewState().showLoadingState())
////                .doOnTerminate(() -> getViewState().hideLoadingstate());
   // }

    public void loadSpinnerData() {
        getViewState().showLoadingState();
        compositeDisposable.add(
                shopRepository.getAllCities()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toFlowable()
                        .flatMapIterable(citiesList -> citiesList)
                        .map(mapList -> {
                            return mapList.get("name").toString();
                        })
                        .toList()
                        .map(citiesList -> {
                            getViewState().setCitiesList(citiesList);
                            return 1;
                        })
                        .observeOn(Schedulers.io())
                        .toMaybe()
                        .flatMap(ignored -> {
                            return shopRepository.getAllShopsNAme();
                        })
                        .toFlowable()
                        .flatMapIterable(shopsNameLIst -> shopsNameLIst)
                        .map(mapList -> {
                            return mapList.get("name").toString();
                        })
                        .toList()
                        .toFlowable()
                        .observeOn(AndroidSchedulers.mainThread())
                        //.doOnSubscribe(c -> getViewState().showLoadingState())
                        //.doOnTerminate(() -> getViewState().hideLoadingstate())
                        .subscribe(shopsNameList -> {
                            getViewState().setShopsNAmeLIst(shopsNameList);
                            getViewState().hideLoadingstate();
                        }, throwable -> {
                            Log.e("mLog", "Setting spinner data");
                            throwable.printStackTrace();
                            getViewState().hideLoadingstate();
                        })
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
//Hierarchical transitions
