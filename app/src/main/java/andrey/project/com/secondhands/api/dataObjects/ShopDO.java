package andrey.project.com.secondhands.api.dataObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ShopDO {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String address;
    Integer updateDay;
    String townName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getUpdateDay() {
        return updateDay;
    }

    public void setUpdateDay(Integer updateDay) {
        this.updateDay = updateDay;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    @Override
    public String toString() {
        return "ShopDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", updateDay=" + updateDay +
                ", townName='" + townName + '\'' +
                '}';
    }
}
