package andrey.project.com.secondhands.repositories.shopsRepository;

import java.util.List;

import andrey.project.com.secondhands.api.dataObjects.ShopDO;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface ShopsDataSource {

    Completable insertShops(List<ShopDO> shopsList);
    Flowable<List<ShopDO>> selectShops();
    Completable deleteShops();
}
