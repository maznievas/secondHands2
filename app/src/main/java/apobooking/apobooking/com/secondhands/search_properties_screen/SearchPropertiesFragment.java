package apobooking.apobooking.com.secondhands.search_properties_screen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import apobooking.apobooking.com.secondhands.MainActivity;
import apobooking.apobooking.com.secondhands.R;
import apobooking.apobooking.com.secondhands.entity.Shop;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchPropertiesFragment extends MvpAppCompatFragment implements SearchPropertiesView {

    @InjectPresenter
    SearchPropertiesPresenter searchPropertiesPresenter;


    private Unbinder unbinder;

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

    public void init(){


        //todo try with proper id
   //    mFirebaseDatabase.push().getKey();

//        Shop shop = new Shop();
//        shop.setId(0);
//        shop.setName("Econom class");
//        shop.setAddress("Test address");
//        shop.setUpdateDay(1);
//
//        Shop shop1 = new Shop();
//        shop1.setId(1);
//        shop1.setName("Massa");
//        shop1.setAddress("Test address 1");
//        shop1.setUpdateDay(0);
//
//        mFirebaseDatabase.child(String.valueOf(shop.getId())).setValue(shop);
//        mFirebaseDatabase.child(String.valueOf(shop1.getId())).setValue(shop1);



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        searchPropertiesPresenter.clear();
    }

    @OnClick(R.id.applyButton)
    public void onApplyClicked()
    {
        ((MainActivity)getActivity()).openMap();
    }
}
