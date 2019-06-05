package andrey.project.com.secondhands.map_screen;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import andrey.project.com.secondhands.SecondHandApplication;
import andrey.project.com.secondhands.entity.City;
import andrey.project.com.secondhands.entity.Shop;
import andrey.project.com.secondhands.entity.ShopName;
import andrey.project.com.secondhands.repositories.shop.ShopRepository;
import andrey.project.com.secondhands.util.Const;
import andrey.project.com.secondhands.util.DayDetectHelper;
import andrey.project.com.secondhands.util.LocationUtil;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Response;

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

    @Inject
    LocationUtil locationUtil;

    private StorageReference gsReference;

    private CompositeDisposable compositeDisposable;

    private Map<Marker, Integer> invisibleMarkersList;// = new HashMap<Marker, Integer>();

    public MapPresenter() {
        SecondHandApplication.getAppComponent().inject(this);
        init();
    }

    public void init() {
        compositeDisposable = new CompositeDisposable();
        invisibleMarkersList = new HashMap<Marker, Integer>();
    }


    public void selectShops(String city, String shopsName, String updateDay, Geocoder geocoder,
                            boolean needLimit, String geocodingApiKey) {
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
////                                getViewState().addSelectedShop(shop);
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
//                    getViewState().addSelectedShop(shop);
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
                        .concatMap(i -> Observable.just(i).delay(250, TimeUnit.MILLISECONDS).toFlowable(BackpressureStrategy.BUFFER))
                        .map(shopMap -> {
                            Shop shop = new Shop();
                            shop.setNameId(shopMap.get(Const.Firebase.NAME_ID).toString());
                            shop.setAddress(shopMap.get(Const.Firebase.ADDRESS).toString());
                            shop.setUpdateDay(Integer.parseInt(shopMap.get(Const.Firebase.UPDATE_DAY).toString()));
                            shop.setImages((ArrayList<String>) shopMap.get(Const.Firebase.IMAGES_ARRAY));

                            return shop;
                        })
                        .flatMap(shop -> {
                            //  return Flowable.just(geocoder.getFromLocationName(shop.getAddress(), 1))
                            return getLocationByName(shop.getAddress(), geocodingApiKey, geocoder)
                                    // .filter(geoList -> geoList.size() > 0)
//                                    .map(geoList -> {
//                                        android.location.Address address1 = geoList.get(0);
//                                        double lat = address1.getLatitude();
//                                        double lng = address1.getLongitude();
//
//                                        return new LatLng(lat, lng);
//                                    })
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
                                                })
                                                .map(shop1 -> {
                                                    Log.d("mLog", "REF: " + Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImageName());
                                                    gsReference = firebaseStorage
                                                            .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImageName());
                                                    shop.setImageReference(gsReference);

                                                    List<StorageReference> storageList = new ArrayList<>();
                                                    for (String imagePath : shop.getImages()) {
                                                        gsReference = firebaseStorage
                                                                .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + imagePath);
                                                        storageList.add(gsReference);
                                                    }
                                                    shop.setImagesReference(storageList);

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
                            getViewState().addSelectedShop(shop);
                        })
                        .subscribe(ignored -> {

                        }, throwable -> {
                            Log.e("mLog", "Test mapping");
                            throwable.printStackTrace();
                        })
        );
    }

    private Single<Response> sdfsdf(Call call) {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(call.execute());
            } catch (Exception ex) {
                emitter.onError(ex);
            }
        });
    }

    public Flowable<LatLng> getLocationByName(String address, String mapsApiKey,
                                              Geocoder geocoder) throws UnsupportedEncodingException {

//        String uri = "https://maps.google.com/maps/api/geocode/json?key=" + mapsApiKey +
//                "&address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false";
//        Log.d("mLog", "url:  " + uri);
//        Request request = new Request.Builder()
//                .url(uri)
//                .build();



        return Flowable.create(emitter -> {
            List<Address> adresses = new ArrayList<>();
            try {
                adresses = geocoder.getFromLocationName(address, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            emitter.onNext(new LatLng(adresses.get(0).getLatitude(), adresses.get(0).getLongitude()));
            emitter.onComplete();

        }, BackpressureStrategy.BUFFER);

//        return Flowable.just(new OkHttpClient())
//                .flatMap(defaultHttpClient -> {
//                    return Flowable.just(defaultHttpClient.newCall(request));
//                })
//                .flatMapSingle(this::sdfsdf)
//                .filter(response -> response != null)
//                .map(response -> {
//                    return response.body().string();
//                })
//                .map(response -> {
//                    return new JSONObject(response);
////                })
//                .map(jsonObject -> {
//                    double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                            .getJSONObject("geometry").getJSONObject("location")
//                            .getDouble("lng");
//
//                    double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                            .getJSONObject("geometry").getJSONObject("location")
//                            .getDouble("lat");
//                    return new LatLng(lat, lng);
//                });


//

    }

    public void clear() {
        compositeDisposable.clear();
    }

    public void displaySelectedShop(String shopId, Geocoder geocoder, String geoCoderApiKey) {
        compositeDisposable.add(shopRepository.getShopById(shopId)
                        .map(shop -> {
                            Log.d("mLog", "REF: " + Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImageName());
                            gsReference = firebaseStorage
                                    .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + shop.getImageName());
                            shop.setImageReference(gsReference);

                            List<StorageReference> storageList = new ArrayList<>();
                            for (String imagePath : shop.getImages()) {
                                gsReference = firebaseStorage
                                        .getReferenceFromUrl(Const.Firebase.BASE_IMAGE_REFERENCE + imagePath);
                                storageList.add(gsReference);
                            }
                            shop.setImagesReference(storageList);

                            return shop;
                        })
                        .flatMap(shop -> {
//                    return Flowable.just(geocoder.getFromLocationName(shop.getAddress(), 1))
//                            .filter(geoList -> geoList.size() > 0)
//                            .map(geoList -> {
//                                android.location.Address address1 = geoList.get(0);
//                                double lat = address1.getLatitude();
//                                double lng = address1.getLongitude();
//
//                                return new LatLng(lat, lng);
//                            })
                            return getLocationByName(shop.getAddress(), geoCoderApiKey, geocoder)
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
                                    })
                                    .subscribeOn(Schedulers.io());
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(c -> getViewState().showLoadingState())
                        .doOnTerminate(() -> getViewState().hideLoadingstate())
                        .subscribe(shop -> {
                            getViewState().showLocation(shop.getLl());
                            getViewState().addSelectedShop(shop);
                        }, throwable -> {
                            Log.e("mLog", "displaying selected shop error");
                            throwable.printStackTrace();
                        })
        );
    }

    public void formInvisibleList(Map<Marker, Shop> markerShopMap, LatLngBounds currentScreen,
                                  LatLng cameraPositionTarget) {
        compositeDisposable.add(Flowable.just(markerShopMap.keySet())
                .flatMapIterable(markers -> markers)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(marker -> {
                    return Flowable.just(marker.getPosition())
                            .observeOn(Schedulers.io())
                            .map(markerPosition -> {
                                if (currentScreen.contains(markerPosition)) {
                                    // marker inside visible region
                                    invisibleMarkersList.remove(marker);
                                } else {
                                    // marker outside visible region
                                    int direction = locationUtil.detectDirection(cameraPositionTarget, markerPosition);
                                    Log.d("mLog2", "direction: " + direction);
                                    invisibleMarkersList.put(marker, direction);
                                }
                                return invisibleMarkersList;
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(invisibleMarkersList -> {
                    getViewState().displayDirections(invisibleMarkersList);
                }, throwable -> {
                    Log.e("mLog", "form invisible list");
                    throwable.printStackTrace();
                })
        );
    }
}
