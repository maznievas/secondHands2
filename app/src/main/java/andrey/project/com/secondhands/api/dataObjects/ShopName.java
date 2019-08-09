package andrey.project.com.secondhands.api.dataObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ShopName {

    @PrimaryKey(autoGenerate = true)
    int id;
    String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ShopName{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
