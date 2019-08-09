package andrey.project.com.secondhands.repositories.shopsRepository.local;

import java.util.List;

import javax.inject.Inject;

import andrey.project.com.secondhands.api.dataObjects.ShopDO;
import andrey.project.com.secondhands.database.AppDatabase;
import andrey.project.com.secondhands.database.ShopDODao;
import andrey.project.com.secondhands.repositories.shopsRepository.ShopsDataSource;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class ShopsLocalDataSource implements ShopsDataSource {

    ShopDODao shopDODao;

    @Inject
    public ShopsLocalDataSource(AppDatabase appDatabase) {
        shopDODao = appDatabase.shopDODao();
    }

    @Override
    public Completable insertShops(List<ShopDO> shopsList) {
        return Completable.fromAction(() -> {
            shopDODao.insertShops(shopsList);
        });
    }

    @Override
    public Flowable<List<ShopDO>> selectShops() {
        return Flowable.create(emitter -> {
            List<ShopDO> shopsList = shopDODao.getShops();
            try{
                emitter.onNext(shopsList);
                emitter.onComplete();
            }catch (Exception e)
            {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable deleteShops() {
        return Completable.fromAction(() -> {
            shopDODao.deleteShops();
        });
    }
}
