package andrey.project.com.secondhands.map_screen;

import android.app.ProgressDialog;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import andrey.project.com.secondhands.MainActivity;
import andrey.project.com.secondhands.R;
import andrey.project.com.secondhands.entity.Shop;
import andrey.project.com.secondhands.util.Const;
import andrey.project.com.secondhands.util.DayDetectHelper;
import andrey.project.com.secondhands.util.LocationUtil;
import andrey.project.com.secondhands.util.OnTouchOutsideViewListener;
import andrey.project.com.secondhands.util.SlidingImageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

/**
 * Created by sts on 21.06.18.
 */

public class MapFragment extends MvpAppCompatFragment implements MapView, OnTouchOutsideViewListener {

    @InjectPresenter
    MapPresenter mapPresenter;

    @Inject
    LocationUtil locationUtil;

    @BindView(R.id.bottom_sheet)
    CoordinatorLayout bottomSheet;

    @BindView(R.id.main_appbar)
    AppBarLayout appBar;

    @BindView(R.id.main_toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @BindView(R.id.main_collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.adressTextView)
    TextView shopAddressTextView;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    CirclePageIndicator circlePageIndicator;

    @BindView(R.id.arrowUpImageView)
    ImageView arrowUpImageView;

    //directions

    @BindView(R.id.eastLayout)
    ViewGroup eastLayout;

    @BindView(R.id.westLayout)
    ViewGroup westLayout;

    @BindView(R.id.northLayout)
    ViewGroup northLayout;

    @BindView(R.id.southLayout)
    ViewGroup southLayout;

    @BindView(R.id.eastTextView)
    TextView eastCounter;

    @BindView(R.id.westTextView)
    TextView westCounter;

    @BindView(R.id.northTextView)
    TextView northCounter;

    @BindView(R.id.southTextView)
    TextView southCounter;

    private Unbinder unbinder;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMapGlobal;
    private Geocoder geocoder;
    private ProgressDialog progressDialog;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Map<Marker, Shop> markerShopMap;
    private SlidingImageAdapter slidingImageAdapter;
    private Animation animUp, animDown;


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

    public void init() {
        markerShopMap = new HashMap<>();

        slidingImageAdapter = new SlidingImageAdapter(getChildFragmentManager());
        viewPager.setAdapter(slidingImageAdapter);
        //    viewPager.setOffscreenPageLimit(-1);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                ((SlidingImageAdapter)viewPager.getAdapter()).getItem(position).refresh();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        circlePageIndicator.setViewPager(viewPager);
        final float density = getResources().getDisplayMetrics().density;
        circlePageIndicator.setRadius(5 * density);

        // bottom sheet pattern
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //      Log.d("BottomSheetBehavior", "state changed: " + newState);
//                if(newState == STATE_COLLAPSED)
//                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                if(newState == STATE_EXPANDED)
//                    toolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //  Log.d("BottomSheetBehavior", "onSlide: " + slideOffset);
            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);



        ((MainActivity) getActivity()).setOnTouchOutsideViewListener(bottomSheet, this);

        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMapGlobal = googleMap;

                googleMapGlobal.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        CameraPosition cameraPosition = googleMapGlobal.getCameraPosition();
                        LatLngBounds currentScreen = googleMapGlobal.getProjection().getVisibleRegion().latLngBounds;

                        mapPresenter.formInvisibleList(markerShopMap, currentScreen, cameraPosition.target);

                       // Log.d("mLog", "Invisible markers' list size: " + invisibleMarkersList.size());
                    }
                });

                ((MainActivity) getActivity()).mapCreated();

                googleMapGlobal.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Log.d("mLog", "on marker clicked");
                        // slidingImageAdapter.clear();

                        Shop shop = markerShopMap.get(marker);
                        toolbarTitle.setText(shop.getName());
                        shopAddressTextView.setText(shop.getAddress());
                        //  slidingImageAdapter.setImageList(shop.getImagesReference());
                        slidingImageAdapter.setImageList(shop.getImages());

                        if (shop.getImagesReference().size() > 0)
                            viewPager.setCurrentItem(0);

                        mBottomSheetBehavior.setState(STATE_COLLAPSED);
                        return true;
                    }
                });

                if (getArguments().getBoolean(Const.Bundle.LOAD_ONE_SHOP, false)) {
                    mapPresenter.displaySelectedShop(getArguments().getString(Const.Bundle.SHOP_ID),
                            geocoder, getString(R.string.geocoding_api_key));
                } else
                    mapPresenter.selectShops(getArguments().getString(Const.Bundle.SHOP_CITY),
                            getArguments().getString(Const.Bundle.SHOP_NAME),
                            getArguments().getString(Const.Bundle.SHOP_UPDATE_DAY),
                            geocoder, false, getString(R.string.geocoding_api_key));
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        animUp = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_anim_up);
        animUp.setFillAfter(true);
        animDown = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_anim_down);
        animDown.setFillAfter(true);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_COLLAPSED)
                    arrowUpImageView.startAnimation(animUp);
                else if (newState == STATE_EXPANDED)
                    arrowUpImageView.startAnimation(animDown);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mapPresenter.clear();
    }

    @Override
    public void addSelectedShop(Shop shop) {
        // Log.d("mLog", "Shop mapped: " + shop.getAddress());
        Marker marker = googleMapGlobal.addMarker(new MarkerOptions().position(shop.getLl())
                .title(shop.getName() + " (" + getString(DayDetectHelper.detectDay(shop.getUpdateDay()))
                        + ")")
                .snippet(shop.getUpdateDay() + " "
                        + shop.getAddress())
        );
        markerShopMap.put(marker, shop);
    }

    @Override
    public void showLocation(LatLng ll) {
        googleMapGlobal.clear();
        CameraUpdate update = null;

        update = CameraUpdateFactory.newLatLngZoom(ll, 10);
        googleMapGlobal.moveCamera(update);
    }

    @Override
    public void showLoadingState() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingstate() {
        progressDialog.dismiss();
    }

    @Override
    public void displayDirections(Map<Marker, Integer> invisibleMarkers) {
        int _northCounter = 0, _southCounter = 0, _eastCounter = 0, _westCounter = 0;
        for(Integer direction : invisibleMarkers.values())
        {
            switch(direction)
            {
                case Const.Direction.EAST:
                    _eastCounter++;
                    break;
                case Const.Direction.NORTH:
                    _northCounter++;
                    break;
                case Const.Direction.WEST:
                    _westCounter++;
                    break;
                case Const.Direction.SOUTH:
                    _southCounter++;
                    break;
            }
        }

        if(_eastCounter != 0) {
            eastLayout.setVisibility(View.VISIBLE);
            eastCounter.setText(String.valueOf(_eastCounter));
        }
        else
            eastLayout.setVisibility(View.INVISIBLE);

        if(_northCounter != 0)
        {
            northLayout.setVisibility(View.VISIBLE);
            northCounter.setText(String.valueOf(_northCounter));
        }
        else
            northLayout.setVisibility(View.INVISIBLE);

        if(_westCounter != 0)
        {
            westLayout.setVisibility(View.VISIBLE);
            westCounter.setText(String.valueOf(_westCounter));
        }
        else
            westLayout.setVisibility(View.INVISIBLE);

        if(_southCounter != 0)
        {
            southLayout.setVisibility(View.VISIBLE);
            southCounter.setText(String.valueOf(_southCounter));
        }
        else
            southLayout.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onTouchOutside(View view, MotionEvent event) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        markerShopMap.clear();
    }


}
