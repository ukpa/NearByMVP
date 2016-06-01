package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import me.unnikrishnanpatel.nearbymvp.R;

public class MainActivity extends AppCompatActivity implements MainViewContract {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    Location mCurrentLocation;
    MainPresenter mainPresenter;
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(this);
        mainPresenter.buildApi(this);
        recyclerView =  (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainPresenter.offlineFeed();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    mainPresenter.startLocationUpdates();

                } else {}
                return;
            }
        }
    }

    @Override
    protected void onStart() {
       mainPresenter.connectApi();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mainPresenter.disconnectApi();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.resumeUpdates();
    }



    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void updatePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

    }



    @Override
    public void catchLocation(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void feedAdapter(final ArrayList<HashMap<String,String>> venues) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new DataAdapter(venues,MainActivity.this));
            }
        });
    }

}
