package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.content.Context;
import android.location.Location;


/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public interface MainViewContract {
    public Context getAppContext();
    public Context getActivityContext();
    public void updatePermissions();
    public void catchLocation(Location location);
}
