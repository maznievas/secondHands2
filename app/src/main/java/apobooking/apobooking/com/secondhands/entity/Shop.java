package apobooking.apobooking.com.secondhands.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.StorageReference;

@IgnoreExtraProperties
@Entity
public class Shop {

    String address;
    @PrimaryKey
    @NonNull
    String id;
    String nameId;
    Integer updateDay;
    String cityId;
    String name;
    @Ignore
    LatLng ll;
    @Ignore
    StorageReference imageReference;
    String imageName;

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

    public Integer getUpdateDay() {
        return updateDay;
    }

    public void setUpdateDay(Integer updateDay) {
        this.updateDay = updateDay;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLl() {
        return ll;
    }

    public void setLl(LatLng ll) {
        this.ll = ll;
    }

    public StorageReference getImageReference() {
        return imageReference;
    }

    public void setImageReference(StorageReference imageReference) {
        this.imageReference = imageReference;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
