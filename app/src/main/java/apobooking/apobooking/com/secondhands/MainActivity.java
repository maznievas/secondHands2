package apobooking.apobooking.com.secondhands;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import apobooking.apobooking.com.secondhands.mainFragment.MainFragment;
import apobooking.apobooking.com.secondhands.map_screen.MapFragment;
import apobooking.apobooking.com.secondhands.search_properties_screen.SearchPropertiesFragment;
import apobooking.apobooking.com.secondhands.util.Const;
import apobooking.apobooking.com.secondhands.util.OnTouchOutsideViewListener;

import static android.content.Context.*;

public class MainActivity extends AppCompatActivity {

    private View mTouchOutsideView;
    private OnTouchOutsideViewListener mOnTouchOutsideViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mainFragment = fragmentManager.findFragmentById(R.id.fragmentHolderLayout);
        //Fragment mainFragment = fragmentManager.findFragmentById(R.id.fragmentHolderLayout);

        if (mainFragment == null) {
            mainFragment = new MainFragment();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .add(R.id.fragmentHolderLayout, mainFragment)
                .addToBackStack(Const.BackStack.MAIN_FRAGMENT)
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

      //  Toolbar topToolBar = (Toolbar)findViewById(R.id.main_toolbar);
      //  setSupportActionBar(topToolBar);
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

    public void mapCreated()
    {
        Toolbar topToolBar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setTitle("TEST TITLE");
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) instanceof SearchPropertiesFragment)
            finish();
        else
            super.onBackPressed();
    }

    /**
     * Sets a listener that is being notified when the user has tapped outside a given view. To remove the listener,

     * <p/>
     * This is useful in scenarios where a view is in edit mode and when the user taps outside the edit mode shall be
     * stopped.
     *
     * @param view
     * @param onTouchOutsideViewListener
     */
    public void setOnTouchOutsideViewListener(View view, OnTouchOutsideViewListener onTouchOutsideViewListener) {
        mTouchOutsideView = view;
        mOnTouchOutsideViewListener = onTouchOutsideViewListener;
    }

    public OnTouchOutsideViewListener getOnTouchOutsideViewListener() {
        return mOnTouchOutsideViewListener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // Notify touch outside listener if user tapped outside a given view
            if (mOnTouchOutsideViewListener != null && mTouchOutsideView != null
                    && mTouchOutsideView.getVisibility() == View.VISIBLE) {
                Rect viewRect = new Rect();
                mTouchOutsideView.getGlobalVisibleRect(viewRect);
                if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    mOnTouchOutsideViewListener.onTouchOutside(mTouchOutsideView, ev);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
