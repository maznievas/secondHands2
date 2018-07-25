package apobooking.apobooking.com.secondhands.map_screen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.google.android.gms.maps.model.LatLng;

import apobooking.apobooking.com.secondhands.entity.Shop;

/**
 * Created by sts on 21.06.18.
 */

@StateStrategyType(SkipStrategy.class)
public interface MapView extends MvpView {
    void addSelectedShop(Shop shop);
    void showLocation(LatLng ll);
    void showLoadingState();
    void hideLoadingstate();
}
