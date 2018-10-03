package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;


import java.util.List;

import apobooking.apobooking.com.secondhands.MainActivity;
import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.ui.CustomLayoutManager;
import apobooking.apobooking.com.secondhands.ui.ShowSelectedShopsButton;
import apobooking.apobooking.com.secondhands.util.Const;
import apobooking.apobooking.com.secondhands.util.MyScrollView;
import apobooking.apobooking.com.secondhands.util.adapters.ShopsAdapter;
import apobooking.apobooking.com.secondhands.util.behaviors.DisableableAppBarLayoutBehavior;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;

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
    MyScrollView nestedScrollView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_appbar)
    AppBarLayout appBarLayout;
    // @BindView(R.id.main_toolbar)
    // Toolbar toolbar;
    @BindView(R.id.main_collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;
    // @BindView(R.id.progressBarRecyclerView)
    // ProgressBar progressBarRV; // todo uncomment
    // @BindView(R.id.parentLayout)
    // ViewGroup parentLayout;
    //  @BindView(R.id.notTouchableLayout)
    //  CustomRelativeLayout notTouchableLayout;


    private ShopsAdapter shopsAdapter;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> citiesAdapter, shopsNameAdapter, updateDayAdapter;
    boolean needToResetLastResult = false;
    CustomLayoutManager mLayoutManager;
    public int positionToScroll;
    CoordinatorLayout.LayoutParams appBarLayoutParams;
    AppBarLayout.LayoutParams toolbarLayoutParams;
    DisableableAppBarLayoutBehavior appBarLayoutBehavior;
    int citySpinnerPosition = 0, shopNameSpinnerPosition = 0, updateDaySpinnerPosition = 0;

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
        View view = inflater.inflate(R.layout.fragment_search_properties_coordinator_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        mLayoutManager = new CustomLayoutManager(getContext());
        selectedShopsRecyclerView.setLayoutManager(mLayoutManager);
        shopsAdapter = new ShopsAdapter(getContext());
        shopsAdapter.setShopItemListener(this);
        selectedShopsRecyclerView.setAdapter(shopsAdapter);
        selectedShopsRecyclerView.setNestedScrollingEnabled(false);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
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
        });

        //tries with toolbar to disable collapse
        toolbarLayoutParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        setAppBarDragEnabled(false);

        // searchPropertiesPresenter.loadSpinnerData();

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
                setAppBarDragEnabled(false);
               // if(position > 0)
                    highlightCityName(false);
//                else
//                    highlightCityName(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                highlightCityName(false);
            }
        });


        shopNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showSelectedShopsButton.setInactive();
                selectedShopsRecyclerView.setVisibility(View.GONE);
                needToResetLastResult = true;
                shopsAdapter.clear();
                setAppBarDragEnabled(false);
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
                setAppBarDragEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       // updateDaySpinner.setSelection(updateDaySpinnerPosition);

        shopsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                //mManager.smoothScrollToPosition(mMessages, null, mAdapter.getItemCount());
                if (itemCount == 1)
                    mLayoutManager.smoothScrollToPosition(selectedShopsRecyclerView, null, shopsAdapter.getItemCount() - 1);
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
    public void onPause() {
        citySpinnerPosition = citySpinner.getSelectedItemPosition();
        shopNameSpinnerPosition = shopNameSpinner.getSelectedItemPosition();
        updateDaySpinnerPosition = updateDaySpinner.getSelectedItemPosition();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        searchPropertiesPresenter.clear();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  outState.putString(Const.SavedInstanceState.CITY_NAME, citiesAdapter.getItem(citySpinner.getSelectedItemPosition()));
        //  outState.putString(Const.SavedInstanceState.SHOP_NAME, shopsNameAdapter.getItem(shopNameSpinner.getSelectedItemPosition()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
        if (shopList.size() > 0)
            shopsAdapter.addSelectedShops(shopList);
        allowToSearch = true;
    }

    @Override
    public void setCitiesList(List<String> citiesList) {
        if (!citiesList.get(0).equals(getContext().getString(R.string.select_any_city)))
            citiesList.add(0, getContext().getString(R.string.select_any_city));
        citiesAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, citiesList);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        citySpinner.setAdapter(citiesAdapter);
        if (citiesList.size() > 0)
            citySpinner.setSelection(citySpinnerPosition);
    }

    @Override
    public void setShopsNAmeLIst(List<String> shopsNameList) {
        if (!shopsNameList.get(0).equals(getContext().getString(R.string.all_shops)))
            shopsNameList.add(0, getContext().getString(R.string.all_shops));
        shopsNameAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, shopsNameList);
        shopsNameAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        shopNameSpinner.setAdapter(shopsNameAdapter);
        if (shopsNameList.size() > 0)
            shopNameSpinner.setSelection(shopNameSpinnerPosition);
    }

    @Override
    public void showMessage(int resId) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(getContext())
                .create();
        alertDialog.setButton("Ok", ((dialog, which) -> dialog.dismiss()));
        alertDialog.setMessage(getString(resId));
        alertDialog.show();
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
        //progressBarRV.setVisibility(View.VISIBLE); //todo uncomment
    }

    @Override
    public void hideProgressBar() {
        //progressBarRV.setVisibility(View.INVISIBLE);//todo uncommet
    }

    @Override
    public void lockUI() {
        Log.d("mLog1", "LOCK");
        //selectedShopsRecyclerView.setNestedScrollingEnabled(true);
        // nestedScrollView.setScrolling(false);
        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        //         WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void unlockUI() {
        Log.d("mLog1", "UNLOCK");

        //selectedShopsRecyclerView.setNestedScrollingEnabled(false);
        //nestedScrollView.setScrolling(true);
        // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void scrollToFindButton() {
        //nestedScrollView.scrollBy((int) showSelectedShopsButton.getX(), (int) showSelectedShopsButton.getY());
    }

    @OnClick(R.id.allShopsLayout)
    public void ShowSelectedShopsButton() {
        if(isCityChecked()) {
            showSelectedShopsButton.changeState();

            if (selectedShopsRecyclerView.getVisibility() == View.VISIBLE) {
                selectedShopsRecyclerView.setVisibility(View.GONE);
                shopsAdapter.clear();
                needToResetLastResult = true;
                setAppBarDragEnabled(false);
            } else {
                selectedShopsRecyclerView.setVisibility(View.VISIBLE);
                appBarLayout.setExpanded(false);
                setAppBarDragEnabled(true);
                //needToResetLastResult = false;
            }
            selectShops(true);
            //paginator.initLoad();
        }
    }

    private boolean isCityChecked() {
        if(citySpinner.getSelectedItemPosition() > 0)
            return true;
        else
        {
            //showMessage(R.string.select_any_city);
            highlightCityName(true);
            return false;
        }
    }

    private void highlightCityName(boolean flag) {
        if (flag)
            citySpinner.setBackground(getResources().getDrawable(R.drawable.highlighted_border));
        else
            citySpinner.setBackground(getResources().getDrawable(R.drawable.yellow_border));

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
        if(isCityChecked()) {
            String city, shopName, updateDay;
            if (citySpinner.getSelectedItemPosition() > 0)
                city = citiesAdapter.getItem(citySpinner.getSelectedItemPosition());
            else
                city = "";
            if (shopNameSpinner.getSelectedItemPosition() > 0)
                shopName = shopsNameAdapter.getItem(shopNameSpinner.getSelectedItemPosition());
            else
                shopName = "";
            if (updateDaySpinner.getSelectedItemPosition() > 0)
                updateDay = updateDayAdapter.getItem(updateDaySpinner.getSelectedItemPosition());
            else
                updateDay = "";
            ((MainActivity) getActivity()).openMap(city, shopName, updateDay);//todo rewrtite with fragmentmanager
        }
    }

    @Override
    public void shopSelected(String shopId) {
        ((MainActivity) getActivity()).openMapSelectedShop(shopId);//todo rewrite with fragmentManager
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

    public void setAppBarDragEnabled(boolean flag) {
        if (!flag)
            toolbarLayoutParams.setScrollFlags(0);
        else
            toolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        collapsingToolbarLayout.setLayoutParams(toolbarLayoutParams);
    }
}
