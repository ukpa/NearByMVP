package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by unnikrishnanpatel on 01/06/16.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        RealmConfiguration realmConfiguration =  new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
