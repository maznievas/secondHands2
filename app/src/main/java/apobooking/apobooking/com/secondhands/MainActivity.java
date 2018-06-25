package apobooking.apobooking.com.secondhands;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import apobooking.apobooking.com.secondhands.map_screen.MapFragment;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesFragment;

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

    public void openMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment mapFragment = MapFragment.newInstance();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.fragmentHolderLayout, mapFragment)
                .addToBackStack("Map")
                .commit();
    }
}
