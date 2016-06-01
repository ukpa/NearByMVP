package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public class MainPresenter implements MainContract,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DataToPresenterContract {

    WeakReference<MainViewContract> mainViewContractWeakReference;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    DataProvider dataProvider;
    Realm realm;

    public MainPresenter(MainViewContract mainViewContract) {
        mainViewContractWeakReference = new WeakReference<MainViewContract>(mainViewContract);
    }

    public MainViewContract getView() throws NullPointerException {
        if (mainViewContractWeakReference != null) {
            return mainViewContractWeakReference.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    @Override
    public void buildApi(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).
                build();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void connectApi() {
        mGoogleApiClient.connect();
    }

    @Override
    public void disconnectApi() {
        mGoogleApiClient.disconnect();

    }

    @Override
    public void resumeUpdates() {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

    }

    @Override
    public void offlineFeed() {
        realm = Realm.getDefaultInstance();
        ArrayList<HashMap<String,String>> vList = new ArrayList<>();
        RealmResults<Venues> realmResults = realm.where(Venues.class).findAll().sort("distance");
        for(int i=0;i<realmResults.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("id",realmResults.get(i).getId());
            hashMap.put("name",realmResults.get(i).getName());
            hashMap.put("category",realmResults.get(i).getCategory());
            hashMap.put("distance",String.valueOf(realmResults.get(i).getDistance()));
            hashMap.put("url",realmResults.get(i).getUrl());
            hashMap.put("now",realmResults.get(i).getNow());
            vList.add(hashMap);
        }
        getView().feedAdapter(vList);
    }

    @Override
    public void getDataFromDatabase() {
        realm = Realm.getDefaultInstance();
        ArrayList<HashMap<String,String>> vList = new ArrayList<>();
        RealmResults<Venues> realmResults = realm.where(Venues.class).findAll().sort("distance");
        for(int i=0;i<realmResults.size();i++){
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("id",realmResults.get(i).getId());
            hashMap.put("name",realmResults.get(i).getName());
            hashMap.put("category",realmResults.get(i).getCategory());
            hashMap.put("distance",String.valueOf(realmResults.get(i).getDistance()));
            hashMap.put("url",realmResults.get(i).getUrl());
            hashMap.put("now",realmResults.get(i).getNow());
            vList.add(hashMap);
        }
        getView().feedAdapter(vList);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(getView().getActivityContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getView().getActivityContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else{
            if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) == null || LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude() != location.getLongitude()
                    && LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude() != location.getLatitude()) {

                getView().catchLocation(location);
                dataProvider = new DataProvider(this);
                String loc = location.getLatitude() + "," + location.getLongitude();
                dataProvider.fetchData(loc);

            } else {
                Log.d("shajhsjahsjahsja","called");
            }

        }

    }


    void startLocationUpdates() {
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(getView().getActivityContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getView().getActivityContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED ) {
                getView().updatePermissions();

            }else{
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

}
