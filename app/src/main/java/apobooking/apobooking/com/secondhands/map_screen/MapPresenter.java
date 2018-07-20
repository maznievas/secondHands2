package apobooking.apobooking.com.secondhands.map_screen;

import android.location.Geocoder;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import apobooking.apobooking.com.secondhands.SecondHandApplication;
import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;
import apobooking.apobooking.com.secondhands.repositories.shop.ShopRepository;
import apobooking.apobooking.com.secondhands.util.Const;
import apobooking.apobooking.com.secondhands.util.DayDetectHelper;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sts on 21.06.18.
 */
@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> {

    @Inject
    FirebaseFirestore firebaseFirestore;
    @Inject
    ShopRepository shopRepository;

    @Inject
    FirebaseStorage firebaseStorage;

    private StorageReference gsReference;

    private CompositeDisposable compositeDisposable;

    public MapPresenter() {
        SecondHandApplication.getAppComponent().inject(this);
        init();
    }

    public void init() {
        compositeDisposable = new CompositeDisposable();
    }



    public void selectShops(String city, String shopsName, String updateDay, Geocoder geocoder, boolean needLimit) {
//        final String[] cityIdFinal = new String[1];
//        final String[] shopsNameIdFinal = new String[1];
//        final boolean[] showLocation = {true};
//
//        compositeDisposable.add(shopRepository.getAllCitiesEntity()
//                        .toFlowable()
//                        .map(allCitiesList -> {
//                            for (City cityMap : allCitiesList) {
//                                if (cityMap.getName().equals(city))
//                                    cityIdFinal[0] = cityMap.getId();
//                            }
//                            return 1;
//                        })
//                        .flatMap(ignored -> {
//                            return shopRepository.getAllShopNameEntity()
//                                    .toFlowable();
//                        })
//                        .map(allShopsNameList -> {
//                            for (ShopName shopNameFirebase : allShopsNameList) {
//                                if (shopNameFirebase.getName().equals(shopsName))
//                                    shopsNameIdFinal[0] = shopNameFirebase.getId();
//                            }
//                            return 1;
//                        })
//                        .flatMap(ignored -> {
//                            return shopRepository.getSelectedShops(cityIdFinal[0], shopsNameIdFinal[0],
//                                    String.valueOf(DayDetectHelper.detectDay(updateDay)), true, needLimit)
//                                    .toFlowable();
//                        })
//                        .flatMapIterable(shopList -> shopList)
//                        .concatMap(i-> Observable.just(i).delay(500, TimeUnit.MILLISECONDS).toFlowable(BackpressureStrategy.BUFFER))
//                        //.toObservable()
//                        // .iter(shopMapList)
//                        .flatMap(shopMap -> {
//                            return Flowable.just(new Shop())
//                                    .map(shop -> {
//                                        shop.setNameId(shopMap.get(Const.Firebase.NAME_ID).toString());
//                                        shop.setAddress(shopMap.get(Const.Firebase.ADDRESS).toString());
//                                        shop.setUpdateDay(Integer.parseInt(shopMap.get(Const.Firebase.UPDATE_DAY).toString()));
//                                        return shop;
//                                    })
////                            .map(shop -> {
////                                return geocoder.getFromLocationName(shop.getAddress(), 1);
////                            })
////                            .filter(geoList -> geoList.size() > 0 )
////                            .map(geoList -> {
////                                android.location.Address address1 = geoList.get(0);
////                                double lat = address1.getLatitude();
////                                double lng = address1.getLongitude();
////
////                                return new LatLng(lat, lng);
////                            }) .map(ll -> {
////                                shop.setLl(ll);
////                                Log.d("mLog", "setLL");
////                                return shop;
////                            })
////                            .map(shop1 -> {
////                                Log.d("mLog", "display");
////                                Log.d("mLog", "ThreadName 2: " + Thread.currentThread().getName());
////                                getViewState().getSelectedShop(shop);
////                                if(showLocation[0])
////                                {
////                                    getViewState().showLocation(shop.getLl());
////                                    showLocation[0] = false;
////                                }
////                                return 1;
////                            });
//                                    .flatMap(shop -> {
//                                        return shopRepository.getShopNameById(shop.getNameId())
//                                                .subscribeOn(Schedulers.io())
//                                                .map(shopName -> {
//                                                    shop.setName(shopName);
//                                                    return shop;
//                                                });
//                                    });
//                        })
//                        .flatMap(shop -> {
//                            return Flowable.just(geocoder.getFromLocationName(shop.getAddress(), 1))
//                                    .filter(geoList -> geoList.size() > 0)
//                                    .map(geoList -> {
//                                        android.location.Address address1 = geoList.get(0);
//                                        double lat = address1.getLatitude();
//                                        double lng = address1.getLongitude();
//
//                                        return new LatLng(lat, lng);
//                                    })
//                                    .map(ll -> {
//                                        shop.setLl(ll);
//                                        Log.d("mLog", "setLL");
//                                        return shop;
//                                    });
//
//                        })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(shop -> {
//                    if (showLocation[0]) {
//                        getViewState().showLocation(shop.getLl());
//                        showLocation[0] = false;
//                    }
//                    getViewState().getSelectedShop(shop);
//                })
////                .doOnSubscribe(v -> getViewState().showLoadingState())
////                .doOnTerminate(() -> getViewState().hideLoadingstate())
//                        .subscribe(shop -> {
//                            //Log.d("mLog", "display");
//                            //Log.d("mLog", "ThreadName 2: " + Thread.currentThread().getName());
//
//                        }, throwable -> {
//                            Log.e("mLog", "Display shop");
//                            throwable.printStackTrace();
//                        })
//        );
        final String[] cityIdFinal = new String[1];
        final String[] shopsNameIdFinal = new String[1];
        final boolean[] showLocation = {true};
        compositeDisposable.add(
                shopRepository.getAllCitiesEntity()
                        .toFlowable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(allCitiesList -> {
                            getViewState().showLoadingState();
                            return allCitiesList;
                        })
                        .observeOn(Schedulers.io())
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
                        .flatMap(list -> {
                            return shopRepository.getSelectedShops(cityIdFinal[0], shopsNameIdFinal[0],
                                    String.valueOf(DayDetectHelper.detectDay(updateDay)), true, needLimit)
                                    .toFlowable();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(shopMapList -> {
                            getViewState().hideLoadingstate();
                            return shopMapList;
                        })
                        .observeOn(Schedulers.io())
                        .flatMapIterable(list -> list)
                        .concatMap(i-> Observable.just(i).delay(250, TimeUnit.MILLISECONDS).toFlowable(BackpressureStrategy.BUFFER))
                        .map(shopMap -> {
                            // Random r = new Random();
                            // int randNum = r.nextInt(5 - 1) + 1;
                            Shop shop = new Shop();
                            shop.setNameId(shopMap.get(Const.Firebase.NAME_ID).toString());
                            shop.setAddress(shopMap.get(Const.Firebase.ADDRESS).toString());
                            //shop.setLl(new LatLng(47.857616 + randNum, 35.237649 + randNum));
                            shop.setUpdateDay(Integer.parseInt(shopMap.get(Const.Firebase.UPDATE_DAY).toString()));
                            return shop;
                        })
                        .flatMap(shop -> {
                            return Flowable.just(geocoder.getFromLocationName(shop.getAddress(), 1))
                                    .filter(geoList -> geoList.size() > 0)
                                    .map(geoList -> {
                                        android.location.Address address1 = geoList.get(0);
                                        double lat = address1.getLatitude();
                                        double lng = address1.getLongitude();

                                        return new LatLng(lat, lng);
                                    })
                                    .map(ll -> {
                                        shop.setLl(ll);
                                        Log.d("mLog", "setLL");
                                        return shop;
                                    })
                                    .flatMap(shop22 -> {
                                        return shopRepository.getShopNameById(shop.getNameId())
                                                .subscribeOn(Schedulers.io())
                                                .map(shopName -> {
                                                    shop.setName(shopName);
                                                    return shop;
                                                });
                                    });

                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(shop -> {
                            Log.d("mLog", "Do on next");
                            if (showLocation[0]) {
                                getViewState().showLocation(shop.getLl());
                                showLocation[0] = false;
                            }
                            getViewState().getSelectedShop(shop);
                        })
                        .subscribe(ignored -> {

                        }, throwable -> {
                            Log.e("mLog", "Test mapping");
                            throwable.printStackTrace();
                        })
        );
    }

    public void clear() {
        compositeDisposable.clear();
    }

    public void displaySelectedShop(String shopId, Geocoder geocoder) {
        compositeDisposable.add(shopRepository.getShopById(shopId)
                .map(shop -> {
                    Log.d("mLog", "REF: " + Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImageName());
                    gsReference = firebaseStorage
                            .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImageName());
                    shop.setImageReference(gsReference);
                    return shop;
                })
                .flatMap(shop -> {
                    return Flowable.just(geocoder.getFromLocationName(shop.getAddress(), 1))
                            .filter(geoList -> geoList.size() > 0)
                            .map(geoList -> {
                                android.location.Address address1 = geoList.get(0);
                                double lat = address1.getLatitude();
                                double lng = address1.getLongitude();

                                return new LatLng(lat, lng);
                            })
                            .map(ll -> {
                                shop.setLl(ll);
                                Log.d("mLog", "setLL");
                                return shop;
                            })
                            .flatMap(shop22 -> {
                                return shopRepository.getShopNameById(shop.getNameId())
                                        .subscribeOn(Schedulers.io())
                                        .map(shopName -> {
                                            shop.setName(shopName);
                                            return shop;
                                        });
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(c -> getViewState().showLoadingState())
                .doOnTerminate(() -> getViewState().hideLoadingstate())
                .subscribe(shop -> {
                    getViewState().showLocation(shop.getLl());
                    getViewState().getSelectedShop(shop);
                }, throwable -> {
                    Log.e("mLog", "displaying selected shop error");
                    throwable.printStackTrace();
                })
        );
    }
}
