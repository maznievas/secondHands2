package apobooking.apobooking.com.secondhands;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import apobooking.apobooking.com.secondhands.map_screen.MapFragment;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesFragment;
import apobooking.apobooking.com.secondhands.util.Const;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment searchPropertiesFragment = fragmentManager.findFragmentById(R.id.fragmentHolderLayout);

        if (searchPropertiesFragment == null) {
            searchPropertiesFragment = SearchPropertiesFragment.newInstance();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .add(R.id.fragmentHolderLayout, searchPropertiesFragment)
                .addToBackStack("SearchProperties")
                .commit();
    }

    public void openMap(String city, String shopName, String updateDay) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment mapFragment = MapFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString(Const.Bundle.SHOP_CITY, city);
        bundle.putString(Const.Bundle.SHOP_NAME, shopName);
        bundle.putString(Const.Bundle.SHOP_UPDATE_DAY, updateDay);

        mapFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.fragmentHolderLayout, mapFragment)
                .addToBackStack("Map")
                .commit();
    }

    public void openMapSelectedShop(String shopId) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment mapFragment = MapFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString(Const.Bundle.SHOP_ID, shopId);
        bundle.putBoolean(Const.Bundle.LOAD_ONE_SHOP, true);

        mapFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.fragmentHolderLayout, mapFragment)
                .addToBackStack("Map")
                .commit();
    }
}
