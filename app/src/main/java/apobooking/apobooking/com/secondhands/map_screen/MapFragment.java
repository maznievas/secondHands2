package apobooking.apobooking.com.secondhands.map_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import apobooking.apobooking.com.secondhands.R;
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
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng latLng = new LatLng(1.289545, 103.849972);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Singapore"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
}
