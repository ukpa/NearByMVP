package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by unnikrishnanpatel on 31/05/16.
 */

public class DataProvider implements PresenterToDataContract {

    public DataProvider() {}
    ArrayList<String> venuesArrayList;
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
                    venuesArrayList = new ArrayList<>();
                    //Log.d("DATA",String.valueOf(jsonObject.getJSONArray("items")));
                    for(int i=0;i<venues.length();i++){
                        JSONObject jsonObject1 = venues.getJSONObject(i).getJSONObject("venue");
                       venuesArrayList.add(jsonObject1.getString("id"));

                    }
                    Log.d("IDS",String.valueOf(venuesArrayList));

                }
                catch (JSONException e){e.printStackTrace();}

            }
        });

    }

    private void addToDatabase(){

    }
}
