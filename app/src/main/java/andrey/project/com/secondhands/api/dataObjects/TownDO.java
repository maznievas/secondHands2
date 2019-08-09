package andrey.project.com.secondhands.api.dataObjects;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TownDO {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;

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

    @Override
    public String toString() {
        return "TownDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
