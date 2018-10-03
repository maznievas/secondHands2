package apobooking.apobooking.com.secondhands.search_properties_screen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import apobooking.apobooking.com.secondhands.entity.City;
import apobooking.apobooking.com.secondhands.entity.Shop;
import apobooking.apobooking.com.secondhands.entity.ShopName;

/**
 * Created by sts on 21.06.18.
 */

@StateStrategyType(SkipStrategy.class)
public interface SearchPropertiesView extends MvpView {
    void setSelectedShops(List<Shop> shopList);

    @StateStrategyType(AddToEndStrategy.class)
    void addSelectedShops(List<Shop> shopList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setCitiesList(List<String> citiesList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setShopsNAmeLIst(List<String> shopNameList);

    @StateStrategyType(SkipStrategy.class)
    void showMessage(int resId);

    // void setSpinnerData(List<String> citiesList, List<String> shopsNameList);
    void showLoadingState();

    void hideLoadingstate();

    void showProgressBar();

    void hideProgressBar();

    void lockUI();

    void unlockUI();

    void scrollToFindButton();
}
