package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public class DataProvider implements PresenterToDataContract {
    MainPresenter mainPresenter;

    public DataProvider(MainPresenter presenter) {
        mainPresenter = presenter;
    }
    ArrayList<HashMap<String,String>> venuesArrayList;
    ArrayList<Venues> venueList;
    Realm realm;
    String url = "https://api.foursquare.com/v2/venues/explore?&oauth_token=2QKJ3SXYVY5MLRI5IKSLRZVB5E301CAPRVQ05EJK45GMAM0L&v=20160531&query=business";
    @Override
    public void fetchData(String location) {
        final String api = url+"&ll="+location;
        OkHttpClient client =  new OkHttpClient();
        Request request = new Request.Builder().
                url(api).
                build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    String data = response.body().string();
                    JSONObject jsonObject = (JSONObject)new JSONObject(data).getJSONObject("response").getJSONArray("groups").get(0);
                    JSONArray venues = jsonObject.getJSONArray("items");
                    venuesArrayList = new ArrayList<HashMap<String, String>>();
                    for(int i=0;i<venues.length();i++){
                        JSONObject jsonObject1 = venues.getJSONObject(i).getJSONObject("venue");
                        HashMap<String,String> hashMap = new HashMap<String, String>();
                        hashMap.put("id",jsonObject1.getString("id"));
                        hashMap.put("distance", String.valueOf(jsonObject1.getJSONObject("location").getInt("distance")));
                       venuesArrayList.add(hashMap);
                    }
                    Log.d("IDS",String.valueOf(venuesArrayList));
                    createVenues();

                }
                catch (JSONException e){e.printStackTrace();}

            }
        });

    }

    private void createVenues(){
        venueList = new ArrayList<>();
        for(int i =0;i<venuesArrayList.size();i++){
            try{
                String url = "https://api.foursquare.com/v2/venues/"+venuesArrayList.get(i).get("id")+"?oauth_token=2QKJ3SXYVY5MLRI5IKSLRZVB5E301CAPRVQ05EJK45GMAM0L&v=20160601";
                Log.d("url",url);
                int distance = Integer.parseInt(venuesArrayList.get(i).get("distance"));
                OkHttpClient client =  new OkHttpClient();
                Request request = new Request.Builder().
                        url(url).
                        build();
                Response response = client.newCall(request).execute();
                JSONObject jsonobject = new JSONObject(response.body().string());
                Venues v = new Venues();
                v.setId(jsonobject.getJSONObject("response").getJSONObject("venue").getString("id"));
                v.setName(jsonobject.getJSONObject("response").getJSONObject("venue").getString("name"));
                v.setDistance(distance);
                if(jsonobject.getJSONObject("response").getJSONObject("venue").has("hours")){
                    v.setNow(String.valueOf(jsonobject.getJSONObject("response").getJSONObject("venue").getJSONObject("hours").get("isOpen")));
                }else{
                    v.setNow("No Data");
                }
                if(jsonobject.getJSONObject("response").getJSONObject("venue").has("bestPhoto")){
                    v.setUrl(jsonobject.getJSONObject("response").getJSONObject("venue").getJSONObject("bestPhoto").getString("prefix")+"100x100"+
                            jsonobject.getJSONObject("response").getJSONObject("venue").getJSONObject("bestPhoto").getString("suffix"));
                }else{
                    v.setUrl("noData");
                }
                if(jsonobject.getJSONObject("response").getJSONObject("venue").getJSONArray("categories").length()>0){
                    v.setCategory(((JSONObject)jsonobject.getJSONObject("response").getJSONObject("venue").getJSONArray("categories").get(0)).getString("name"));
                }
                else{
                    v.setCategory("Default");
                }



                venueList.add(v);

            }catch (Exception e){e.printStackTrace();}
        }
        Log.d("Venues Created","ahsjahsjha");
        addToDatabase();
    }

    private void addToDatabase(){
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        for(int i = 0;i<venueList.size();i++){
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(venueList.get(i));
            realm.commitTransaction();
        }
        mainPresenter.getDataFromDatabase();
        Log.d("ADDED TO DATABASE","fuck yes");
    }
}
