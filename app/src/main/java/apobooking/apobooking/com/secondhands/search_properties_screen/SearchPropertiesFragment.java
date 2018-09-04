package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;


import java.util.ArrayList;
import java.util.List;

import apobooking.apobooking.com.secondhands.MainActivity;
import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.ui.CustomCoordinatorLayout;
import apobooking.apobooking.com.secondhands.ui.CustomLayoutManager;
import apobooking.apobooking.com.secondhands.ui.CustomRelativeLayout;
import apobooking.apobooking.com.secondhands.ui.LockableScrollView;
import apobooking.apobooking.com.secondhands.ui.ShowSelectedShopsButton;
import apobooking.apobooking.com.secondhands.util.Const;
import apobooking.apobooking.com.secondhands.util.ShopsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Flowable;

import static android.view.View.FOCUS_DOWN;

public class SearchPropertiesFragment extends MvpAppCompatFragment implements
        SearchPropertiesView, ShopsAdapter.ShopItemListener {

    public static boolean allowToSearch = true;
    @InjectPresenter
    SearchPropertiesPresenter searchPropertiesPresenter;
    @BindView(R.id.alShopsRecyclerView)
    RecyclerView selectedShopsRecyclerView;
    @BindView(R.id.allShopsLayout)
    ShowSelectedShopsButton showSelectedShopsButton;
    @BindView(R.id.updateDaySpinner)
    Spinner updateDaySpinner;
    @BindView(R.id.citySpinner)
    Spinner citySpinner;
    @BindView(R.id.shopNameSpinner)
    Spinner shopNameSpinner;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.progressBarRecyclerView)
    ProgressBar progressBarRV;
    @BindView(R.id.parentLayout)
    ViewGroup parentLayout;
    @BindView(R.id.notTouchableLayout)
    CustomRelativeLayout notTouchableLayout;


    private ShopsAdapter shopsAdapter;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> citiesAdapter, shopsNameAdapter, updateDayAdapter;
    boolean needToResetLastResult = false;
    CustomLayoutManager mLayoutManager;
    public int positionToScroll;
    // private boolean needLoadingFooter = true;

    public static SearchPropertiesFragment newInstance() {
        return new SearchPropertiesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        positionToScroll = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_properties, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
         // CustomLayoutManager mLayoutManager = new CustomLayoutManager(getContext());
        mLayoutManager = new CustomLayoutManager(getContext());
        selectedShopsRecyclerView.setLayoutManager(mLayoutManager);
        shopsAdapter = new ShopsAdapter(getContext());
        shopsAdapter.setShopItemListener(this);
        selectedShopsRecyclerView.setAdapter(shopsAdapter);
        selectedShopsRecyclerView.setNestedScrollingEnabled(false);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        // if (isLoadData()) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            Log.d("mLog", "Condition to load data: " + String.valueOf(visibleItemCount + pastVisiblesItems)
                                    + " >= " + String.valueOf(totalItemCount));
                            needToResetLastResult = false;
                            selectShops(false);
                        }
                        //  }
                    }
                }
            }
        });

        String[] updateDayList = getResources().getStringArray(R.array.days_of_week);
        updateDayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, updateDayList);
        updateDayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        updateDaySpinner.setAdapter(updateDayAdapter);
        if (updateDayList.length > 0)
            updateDaySpinner.setSelection(0);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        //searchPropertiesPresenter.loadSpinnerData();

        //paginator = new Paginator(getContext(), pullToLoadView, this);
        // paginator.initializePaginator();

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showSelectedShopsButton.setInactive();
                selectedShopsRecyclerView.setVisibility(View.GONE);
                needToResetLastResult = true;
                shopsAdapter.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        shopNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showSelectedShopsButton.setInactive();
                selectedShopsRecyclerView.setVisibility(View.GONE);
                needToResetLastResult = true;
                shopsAdapter.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        updateDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showSelectedShopsButton.setInactive();
                selectedShopsRecyclerView.setVisibility(View.GONE);
                needToResetLastResult = true;
                shopsAdapter.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        shopsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                //mManager.smoothScrollToPosition(mMessages, null, mAdapter.getItemCount());
                if(itemCount == 1)
                    mLayoutManager.smoothScrollToPosition(selectedShopsRecyclerView, null, shopsAdapter.getItemCount()-1);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        searchPropertiesPresenter.clear();
    }

    @OnClick(R.id.applyButton)
    public void onApplyClicked() {
        //   ((MainActivity) getActivity()).openMap();
    }

    @Override
    public void setSelectedShops(List<Shop> shopList) {
        shopsAdapter.setShopList(shopList);
    }

    @Override
    public void addSelectedShops(List<Shop> shopList) {
        if (shopsAdapter.getLoadingFooterState()) {
            shopsAdapter.removeLoadingFooter();
        }
        shopsAdapter.addSelectedShops(shopList);
        allowToSearch = true;
    }

    @Override
    public void setCitiesList(List<String> citiesList) {
        citiesList.add(0, getContext().getString(R.string.all_cities));
        citiesAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, citiesList);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(citiesAdapter);
        if (citiesList.size() > 0)
            citySpinner.setSelection(0);
    }

    @Override
    public void setShopsNAmeLIst(List<String> shopsNameList) {
        shopsNameList.add(0, getContext().getString(R.string.all_shops));
        shopsNameAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, shopsNameList);
        shopsNameAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        shopNameSpinner.setAdapter(shopsNameAdapter);
        if (shopsNameList.size() > 0)
            shopNameSpinner.setSelection(0);
    }

    @Override
    public void showLoadingState() {
        Log.d("mLog", "Show");
        progressDialog.show();
    }

    @Override
    public void hideLoadingstate() {
        Log.d("mLog", "Hide");
        progressDialog.dismiss();
    }

    @Override
    public void showProgressBar() {
        progressBarRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarRV.setVisibility(View.INVISIBLE);
    }

    @Override
    public void lockUI() {
        Log.d("mLog1", "LOCK");
       // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
       //         WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
         }

    @Override
    public void unlockUI() {
        Log.d("mLog1", "UNLOCK");
       // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
       }

    @Override
    public void scrollToFindButton() {
        nestedScrollView.scrollBy((int) showSelectedShopsButton.getX(), (int) showSelectedShopsButton.getY());
    }

    @OnClick(R.id.allShopsLayout)
    public void ShowSelectedShopsButton() {
        showSelectedShopsButton.changeState();

        if (selectedShopsRecyclerView.getVisibility() == View.VISIBLE) {
            selectedShopsRecyclerView.setVisibility(View.GONE);
            shopsAdapter.clear();
            needToResetLastResult = true;
        } else {
            selectedShopsRecyclerView.setVisibility(View.VISIBLE);
            //needToResetLastResult = false;
        }
        selectShops(true);
        //paginator.initLoad();
    }

    public void selectShops(boolean needToScroll) {
        Log.d("mLog", "select shop");
        if (showSelectedShopsButton.getArrowDown()) {
            if (!shopsAdapter.getLoadingFooterState()) {
                shopsAdapter.addLoadingFooter();
                //lockUI();
            }
            String city, shopName, updateDay;
            if (citySpinner.getSelectedItemPosition() != 0)
                city = citiesAdapter.getItem(citySpinner.getSelectedItemPosition());
            else
                city = "";
            if (shopNameSpinner.getSelectedItemPosition() != 0)
                shopName = shopsNameAdapter.getItem(shopNameSpinner.getSelectedItemPosition());
            else
                shopName = "";
            if (updateDaySpinner.getSelectedItemPosition() != 0)
                updateDay = updateDayAdapter.getItem(updateDaySpinner.getSelectedItemPosition());
            else
                updateDay = "";
            searchPropertiesPresenter.selectShops(city, shopName, updateDay, needToResetLastResult, true,
                    needToScroll);
            //allowToSearch = true;
        }
    }

    @OnClick(R.id.showOnMapButton)
    public void onShowOnMapButtonClicked() {
        String city, shopName, updateDay;
        if (citySpinner.getSelectedItemPosition() != 0)
            city = citiesAdapter.getItem(citySpinner.getSelectedItemPosition());
        else
            city = "";
        if (shopNameSpinner.getSelectedItemPosition() != 0)
            shopName = shopsNameAdapter.getItem(shopNameSpinner.getSelectedItemPosition());
        else
            shopName = "";
        if (updateDaySpinner.getSelectedItemPosition() != 0)
            updateDay = updateDayAdapter.getItem(updateDaySpinner.getSelectedItemPosition());
        else
            updateDay = "";
        ((MainActivity) getActivity()).openMap(city, shopName, updateDay);
    }

    @Override
    public void shopSelected(String shopId) {
        ((MainActivity) getActivity()).openMapSelectedShop(shopId);
    }

    @Override
    public void scrollToBottom() {
//        // Scroll to bottom on new messages
//
//          selectedShopsRecyclerView.requestFocus(FOCUS_DOWN);
//        nestedScrollView.fullScroll(View.FOCUS_DOWN);
//        if(shopsAdapter.getItemCount() > 0)
//        ((selectedShopsRecyclerView.getLayoutManager()))
//                .smoothScrollToPosition(selectedShopsRecyclerView, new RecyclerView.State(),
//                        shopsAdapter.getItemCount() - 1);

       // mLayoutManager.scrollToPositionWithOffset(shopsAdapter.getItemCount(), 200);

       // selectedShopsRecyclerView.smoothScrollToPosition(shopsAdapter.getItemCount() - 2);

//        ViewGroup.MarginLayoutParams marginLayoutParams =
//                (ViewGroup.MarginLayoutParams) selectedShopsRecyclerView.getLayoutParams();
//        marginLayoutParams.setMargins(0, 0, 0, 100);
//        selectedShopsRecyclerView.setLayoutParams(marginLayoutParams);
    }

    @Override
    public void scrollTo(int position) {
        positionToScroll = position;
        //    mLayoutManager.scrollToPositionWithOffset(position, 200);
    }
}
