package andrey.project.com.secondhands.repositories.shopsRepository;

import java.util.List;

import javax.inject.Inject;

import andrey.project.com.secondhands.api.dataObjects.ShopDO;
import andrey.project.com.secondhands.repositories.Local;
import andrey.project.com.secondhands.repositories.shopsRepository.local.ShopsLocalDataSource;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public class ShopsRepository implements ShopsDataSource {

    ShopsLocalDataSource shopsDataSource;

    @Inject
    public ShopsRepository(ShopsLocalDataSource shopsDataSource) {
        this.shopsDataSource = shopsDataSource;
    }

    @Override
    public Completable insertShops(List<ShopDO> shopsList) {
        return deleteShops().andThen(shopsDataSource.insertShops(shopsList));
    }

    @Override
    public Flowable<List<ShopDO>> selectShops() {
        return shopsDataSource.selectShops();
    }

    @Override
    public Completable deleteShops() {
        return shopsDataSource.deleteShops();
    }
}
