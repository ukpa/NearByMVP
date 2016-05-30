package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.content.Context;

/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public interface MainContract {

    public void buildApi(Context context);
    public void connectApi();
    public void disconnectApi();
    public void resumeUpdates();

}
