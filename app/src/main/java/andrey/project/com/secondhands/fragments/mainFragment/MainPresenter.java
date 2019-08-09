package andrey.project.com.secondhands.fragments.mainFragment;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import andrey.project.com.secondhands.R;
import andrey.project.com.secondhands.SecondHandApplication;
import andrey.project.com.secondhands.api.dataObjects.ShopDO;
import andrey.project.com.secondhands.api.dataObjects.ShopName;
import andrey.project.com.secondhands.api.dataObjects.TownDO;
import andrey.project.com.secondhands.entity.Shop;
import andrey.project.com.secondhands.repositories.helpfulRepository.HelpfulRepository;
import andrey.project.com.secondhands.repositories.shopsRepository.ShopsRepository;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainFragmentView> {

    private CompositeDisposable compositeDisposable;
    private final String TAG = "MainPresenter";

    @Inject
    HelpfulRepository helpfulRepository;

    @Inject
    ShopsRepository shopsRepository;

    public MainPresenter() {
        compositeDisposable = new CompositeDisposable();
        SecondHandApplication.getAppComponent().inject(this);
        test();
    }

    public void test() {
        Log.d(TAG, "test");
        List<ShopDO> shopDOList = new ArrayList<>();
        List<TownDO> townDOList = new ArrayList<>();
        List<ShopName> shopNameList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ShopDO shopDO = new ShopDO();
            shopDO.setAddress("Address " + i);
            shopDO.setName("Name " + i);
            shopDO.setTownName("Zaporizhzhya");
            shopDO.setUpdateDay(0);

            TownDO townDO = new TownDO();
            townDO.setName("Zaporizhzhya " + i);

            ShopName shopName = new ShopName();
            shopName.setValue("Econom class " + i);

            shopDOList.add(shopDO);
            townDOList.add(townDO);
            shopNameList.add(shopName);
        }

        compositeDisposable.add(
                shopsRepository.insertShops(shopDOList)
                        .andThen(helpfulRepository.insertShopNames(shopNameList))
                        .andThen(helpfulRepository.insertTownDos(townDOList))
                        .andThen(displayShops())
                        .flatMapCompletable(__ -> shopsRepository.deleteShops())
                        .andThen(helpfulRepository.deleteShopNames())
                        .andThen(helpfulRepository.deleteTownDOs())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(__ -> {
                            Log.d(TAG, "subscribe");
                        }, throwable -> {
                            Log.e(TAG, "Error", throwable);
                        })
        );
    }

    public Flowable<Integer> displayShops() {
        return Flowable.zip(shopsRepository.selectShops(), helpfulRepository.getShopNames(),
                helpfulRepository.getTownDOs(),
                (Function3<List<ShopDO>, List<ShopName>, List<TownDO>, Integer>)
                        (_shopsList, _shopNameList, _townDOS) -> {
                            for (int i = 0; i < 10; i++) {
                                Log.d(TAG, _shopsList.get(i).toString());
                                Log.d(TAG, _shopNameList.get(i).toString());
                                Log.d(TAG, _townDOS.get(i).toString());
                            }
                            return 1;
                        }
        );
    }
}
