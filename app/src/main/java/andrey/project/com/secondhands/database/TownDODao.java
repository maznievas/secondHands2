package andrey.project.com.secondhands.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import andrey.project.com.secondhands.api.dataObjects.TownDO;

@Dao
public interface TownDODao {

    @Insert
    void insertTownDOs(List<TownDO> townDOsList);

    @Query("SELECT * FROM TownDO")
    List<TownDO> getTownDOs();

    @Query("DELETE FROM TownDO")
    void deletTownDos();
}
