package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmResults;


/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public interface MainViewContract {
    public Context getAppContext();
    public Context getActivityContext();
    public void updatePermissions();
    public void catchLocation(Location location);
    public void feedAdapter(ArrayList<HashMap<String,String>> venues);
}
