package andrey.project.com.secondhands.search_properties_screen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import andrey.project.com.secondhands.entity.Shop;

/**
 * Created by sts on 21.06.18.
 */

@Deprecated
@StateStrategyType(SkipStrategy.class)
public interface SearchPropertiesView extends MvpView{
    void setSelectedShops(List<Shop> shopList);
    @StateStrategyType(AddToEndStrategy.class)
    void addSelectedShops(List<Shop> shopList);
    @StateStrategyType(AddToEndStrategy.class)
    void setCitiesList(List<String> citiesList);
    @StateStrategyType(AddToEndStrategy.class)
    void setShopsNAmeLIst(List<String> shopNameList);
   // void setSpinnerData(List<String> citiesList, List<String> shopsNameList);
    void showLoadingState();
    void hideLoadingstate();
    void showProgressBar();
    void hideProgressBar();
    void lockUI();
    void unlockUI();

    void scrollToFindButton();
}
