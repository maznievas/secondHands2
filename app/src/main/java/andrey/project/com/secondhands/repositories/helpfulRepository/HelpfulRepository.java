package andrey.project.com.secondhands.repositories.helpfulRepository;

import java.util.List;

import javax.inject.Inject;

import andrey.project.com.secondhands.api.dataObjects.ShopName;
import andrey.project.com.secondhands.api.dataObjects.TownDO;
import andrey.project.com.secondhands.repositories.helpfulRepository.local.HelpfulLocalDataSource;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class HelpfulRepository implements HelpfulDataSource {

    HelpfulLocalDataSource helpfulLocalDataSource;

    @Inject
    public HelpfulRepository(HelpfulLocalDataSource helpfulLocalDataSource) {
        this.helpfulLocalDataSource = helpfulLocalDataSource;
    }

    @Override
    public Completable insertShopNames(List<ShopName> shopNames) {
        return deleteShopNames()
                .andThen(helpfulLocalDataSource.insertShopNames(shopNames));
    }

    @Override
    public Completable deleteShopNames() {
        return helpfulLocalDataSource.deleteShopNames();
    }

    @Override
    public Flowable<List<ShopName>> getShopNames() {
        return helpfulLocalDataSource.getShopNames();
    }

    @Override
    public Completable insertTownDos(List<TownDO> townDOList) {
        return deleteTownDOs()
                .andThen(helpfulLocalDataSource.insertTownDos(townDOList));
    }

    @Override
    public Completable deleteTownDOs() {
        return helpfulLocalDataSource.deleteTownDOs();
    }

    @Override
    public Flowable<List<TownDO>> getTownDOs() {
        return helpfulLocalDataSource.getTownDOs();
    }
}
