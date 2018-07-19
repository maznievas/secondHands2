package apobooking.apobooking.com.secondhands.map_screen;

import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.util.Const;
import apobooking.apobooking.com.secondhands.util.DayDetectHelper;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sts on 21.06.18.
 */

public class MapFragment extends MvpAppCompatFragment implements MapView {

    @InjectPresenter
    MapPresenter mapPresenter;

    private Unbinder unbinder;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMapGlobal;
    private Geocoder geocoder;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init(){
        geocoder = new Geocoder(getContext());
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMapGlobal = googleMap;
//                mapPresenter.selectShops(getArguments().getString(Const.Bundle.SHOP_CITY),
//                        getArguments().getString(Const.Bundle.SHOP_NAME),
//                        getArguments().getString(Const.Bundle.SHOP_UPDATE_DAY),
//                        geocoder, false);
                mapPresenter.selectShops(getArguments().getString(Const.Bundle.SHOP_CITY),
                        getArguments().getString(Const.Bundle.SHOP_NAME),
                        getArguments().getString(Const.Bundle.SHOP_UPDATE_DAY),
                        geocoder, false);
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mapPresenter.clear();
    }

    @Override
    public void displayShop(Shop shop) {
       // Log.d("mLog", "Shop mapped: " + shop.getAddress());
        googleMapGlobal.addMarker(new MarkerOptions().position(shop.getLl())
                        .title(shop.getName() + " (" + getString(DayDetectHelper.detectDay(shop.getUpdateDay()))
                        + ")")
                        .snippet(shop.getUpdateDay() + " "
                                + shop.getAddress())
        );
    }

    @Override
    public void showLocation(LatLng ll) {
        googleMapGlobal.clear();
        CameraUpdate update = null;

        update = CameraUpdateFactory.newLatLngZoom(ll, 10);
        googleMapGlobal.moveCamera(update);
    }


}
