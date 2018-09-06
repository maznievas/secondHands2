package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

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

    @Inject
    FirebaseStorage firebaseStorage;


    private StorageReference gsReference;
    CompositeDisposable compositeDisposable;
    private StorageReference storageRef;
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
//                String cityId = data.getValue().toString();
//                citiesList.add(cityId);
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

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadSpinnerData();
    }

    public void init() {
        compositeDisposable = new CompositeDisposable();

        storageRef = firebaseStorage.getReference();


        // Create a reference to a file from a Google Cloud Storage URI

        //    databaseReference.addListenerForSingleValueEvent(postListener);

        //todo:uncomment for adding data to firestore
//        Map<String, Object> data = new HashMap<>();
//        data.put("nameId", "7GYe0P6TrjKD6FYUHB6m");
//        data.put("address", "");
//        data.put("cityId", "qcFHZtVdGmmDGKpTGAbC");
//        data.put("updateDayId", 0);
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
             boolean needLimit, boolean needToScroll) {
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
                                shop.setId(shopMap.get(Const.Firebase.SHOP_ID).toString());
                                shop.setImagePath(shopMap.get(Const.Firebase.IMAGE_PATH).toString());
                                shop.setNameId(shopMap.get(Const.Firebase.NAME_ID).toString());
                                shop.setAddress(shopMap.get(Const.Firebase.ADDRESS).toString());
                                shop.setImages((ArrayList<String>)shopMap.get(Const.Firebase.IMAGES_ARRAY));
                                shop.setUpdateDay(Integer.parseInt(shopMap.get(Const.Firebase.UPDATE_DAY).toString()));
                                return shop;
                            })
                            .map(shop -> {
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
                .doOnSubscribe(v -> getViewState().lockUI())
                .doOnTerminate(() -> getViewState().unlockUI())
                .subscribe(shopList -> {
                    getViewState().addSelectedShops(shopList);
                   // if(needToScroll)
                    //    getViewState().scrollToFindButton();
                }, throwable -> {
                    Log.e("mLog", "Update shops");
                    throwable.printStackTrace();
                })
        );
    }

 //   public Flowable<List<Shop>> selectShopsFlowable(String cityId, String shopsName, String updateDayId)
  //  {
//        final String[] cityIdFinal = new String[1];
//        final String[] shopsNameIdFinal = new String[1];
//        return shopRepository.getAllCitiesEntity()
//                .toFlowable()
//                //  .flatMapIterable(cityList -> cityList)
//                .map(allCitiesList -> {
//                    for (City cityMap : allCitiesList) {
//                        if (cityMap.getName().equals(cityId))
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
//                            String.valueOf(DayDetectHelper.detectDay(updateDayId)))
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
//                                        .map(shopNameId -> {
//                                            shop.setName(shopNameId);
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
        compositeDisposable.add(
                shopRepository.getAllCities()
                        .toFlowable()
                        .flatMapIterable(citiesList -> citiesList)
                        .map(mapList -> {
                            return mapList.get(Const.Firebase.CITIES_NAME).toString();
                        })
                        .toList()
                        .map(citiesList -> {
                            getViewState().setCitiesList(citiesList);
                            return 1;
                        })
                        .toMaybe()
                        .flatMap(ignored -> {
                            return shopRepository.getAllShopsNAme();
                        })
                        .toFlowable()
                        .flatMapIterable(shopsNameLIst -> shopsNameLIst)
                        .map(mapList -> {
                            return mapList.get(Const.Firebase.SHOPS_NAME_NAME).toString();
                        })
                        .toList()
                        .toFlowable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(c -> {
                            Log.d("mLog", "doOnSubscribe");
                            getViewState().showLoadingState();
                        })
                        .doAfterTerminate(() -> {
                            Log.d("mLog", "doAfterTerminate");
                            getViewState().hideLoadingstate();
                        })
                        .subscribe(shopsNameList -> {
                            getViewState().setShopsNAmeLIst(shopsNameList);
                        }, throwable -> {
                            Log.e("mLog", "Setting spinner data");
                            throwable.printStackTrace();
                        })
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }

    public void getIdsOfSelectedData(String city, String shopName, String updateDay) {
        String[] cityIdFinal = new String[1];
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
                        if (shopNameFirebase.getName().equals(shopName))
                            return shopNameFirebase.getId();
                    }
                    return "";
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shopNameId -> {
                    String cityId = "";
                    if(cityIdFinal[0] != null)
                        cityId = cityIdFinal[0];

                    getViewState().submitListWithProperIds(cityId, shopNameId, DayDetectHelper.detectDay(updateDay));
                }, throwable -> {
                    Log.e("mLog", "Detecting id's of data");
                    throwable.printStackTrace();
                })
        );
    }
}
//Hierarchical transitions
