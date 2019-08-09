package andrey.project.com.secondhands.repositories.helpfulRepository;

import java.util.List;

import andrey.project.com.secondhands.api.dataObjects.ShopName;
import andrey.project.com.secondhands.api.dataObjects.TownDO;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface HelpfulDataSource {

    Completable insertShopNames(List<ShopName> shopNames);
    Completable deleteShopNames();
    Flowable<List<ShopName>> getShopNames();

    Completable insertTownDos(List<TownDO> townDOList);
    Completable deleteTownDOs();
    Flowable<List<TownDO>> getTownDOs();
}
