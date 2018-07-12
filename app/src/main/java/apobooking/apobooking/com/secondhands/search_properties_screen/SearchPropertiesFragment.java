package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;
import java.util.Map;

import apobooking.apobooking.com.secondhands.MainActivity;
import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.ui.ShowSelectedShopsButton;
import apobooking.apobooking.com.secondhands.util.ShopsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchPropertiesFragment extends MvpAppCompatFragment implements SearchPropertiesView {

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

    private ShopsAdapter shopsAdapter;
    private Unbinder unbinder;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> citiesAdapter, shopsNameAdapter, updateDayAdapter;

    public static SearchPropertiesFragment newInstance() {
        return new SearchPropertiesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        selectedShopsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shopsAdapter = new ShopsAdapter(getContext());
        selectedShopsRecyclerView.setAdapter(shopsAdapter);

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

        searchPropertiesPresenter.loadSpinnerData();

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showSelectedShopsButton.setInactive();
                selectedShopsRecyclerView.setVisibility(View.GONE);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        ((MainActivity) getActivity()).openMap();
    }

    @Override
    public void setSelectedShops(List<Shop> shopList) {
        shopsAdapter.setShopList(shopList);
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

    //@Override
    public void setSpinnerData(List<String> citiesList, List<String> shopsNameList) {


    }

    @Override
    public void showLoadingState() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingstate() {
        progressDialog.dismiss();
    }

    @OnClick(R.id.allShopsLayout)
    public void ShowSelectedShopsButton() {
        showSelectedShopsButton.changeState();
        if (selectedShopsRecyclerView.getVisibility() == View.VISIBLE)
            selectedShopsRecyclerView.setVisibility(View.GONE);
        else
            selectedShopsRecyclerView.setVisibility(View.VISIBLE);
        if(showSelectedShopsButton.getArrowDown()) {
            shopsAdapter.clear();
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
            searchPropertiesPresenter.selectShops(city, shopName, updateDay);
        }
    }
}
