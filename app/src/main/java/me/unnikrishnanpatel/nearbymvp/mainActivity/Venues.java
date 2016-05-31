package me.unnikrishnanpatel.nearbymvp.mainActivity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public class Venues extends RealmObject {
    public Venues() {
    }

    @PrimaryKey
    private String id;
    private int distance;
    private String category;
    private String name;
    private String url;
    private String now;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }
}
