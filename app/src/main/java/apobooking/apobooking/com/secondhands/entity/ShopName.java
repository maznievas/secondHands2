package apobooking.apobooking.com.secondhands.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by procreationsmac on 09/07/2018.
 */

@Entity
public class ShopName {

    @PrimaryKey
    @NonNull
    String id;
    String name;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
