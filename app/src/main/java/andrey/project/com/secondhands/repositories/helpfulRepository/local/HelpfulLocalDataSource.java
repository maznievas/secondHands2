package andrey.project.com.secondhands.repositories.helpfulRepository.local;

import java.util.List;

import javax.inject.Inject;

import andrey.project.com.secondhands.api.dataObjects.ShopName;
import andrey.project.com.secondhands.api.dataObjects.TownDO;
import andrey.project.com.secondhands.database.AppDatabase;
import andrey.project.com.secondhands.database.ShopNameDao;
import andrey.project.com.secondhands.database.TownDODao;
import andrey.project.com.secondhands.repositories.helpfulRepository.HelpfulDataSource;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class HelpfulLocalDataSource implements HelpfulDataSource {

    private TownDODao townDODao;
    private ShopNameDao shopNameDao;

    @Inject
    public HelpfulLocalDataSource(AppDatabase appDatabase)
    {
        townDODao = appDatabase.townDODao();
        shopNameDao = appDatabase.shopNameDao();
    }

    @Override
    public Completable insertShopNames(List<ShopName> shopNames) {
        return Completable.fromAction(() -> {
            shopNameDao.insertShopNames(shopNames);
        });
    }

    @Override
    public Completable deleteShopNames() {
        return Completable.fromAction(() -> {
            shopNameDao.deleteShopNames();
        });
    }

    @Override
    public Flowable<List<ShopName>> getShopNames() {
        return Flowable.create(emitter -> {
            List<ShopName> shopNameList = shopNameDao.getShopNames();
            try{
                emitter.onNext(shopNameList);
                emitter.onComplete();
            }catch (Exception e)
            {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable insertTownDos(List<TownDO> townDOList) {
        return Completable.fromAction(() -> {
            townDODao.insertTownDOs(townDOList);
        });
    }

    @Override
    public Completable deleteTownDOs() {
        return Completable.fromAction(() -> {
            townDODao.deletTownDos();
        });
    }

    @Override
    public Flowable<List<TownDO>> getTownDOs() {
        return Flowable.create(emitter -> {
            List<TownDO> townDOList = townDODao.getTownDOs();
            try{
                emitter.onNext(townDOList);
                emitter.onComplete();
            }
            catch (Exception e)
            {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }
}
