package apobooking.apobooking.com.secondhands.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public class Shop {

    String address;
    @PrimaryKey
    @NonNull
    String id;
    String name;
    Integer updateDay;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUpdateDay() {
        return updateDay;
    }

    public void setUpdateDay(Integer updateDay) {
        this.updateDay = updateDay;
    }
}
