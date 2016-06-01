package me.unnikrishnanpatel.nearbymvp.mainActivity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import me.unnikrishnanpatel.nearbymvp.R;

/**
 * Created by unnikrishnanpatel on 01/06/16.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<HashMap<String,String>> mDataset;
    private Context mContext;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView distance;
        public TextView category;
        public ImageView image;
        public TextView now;

        public ViewHolder(View v) {
            super(v);
            name = (TextView)v.findViewById(R.id.name);
            distance = (TextView)v.findViewById(R.id.distance);
            category = (TextView)v.findViewById(R.id.category);
            image = (ImageView) v.findViewById(R.id.image);
            now = (TextView)v.findViewById(R.id.now);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DataAdapter(ArrayList<HashMap<String,String>> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.get(position).get("name"));
        holder.category.setText(mDataset.get(position).get("category"));
        Picasso.with(mContext).load(mDataset.get(position).get("url")).placeholder(R.mipmap.ic_launcher).into(holder.image);
        DecimalFormat decimal = new DecimalFormat("0.00");

        String formattedValue = decimal.format((float) Integer.parseInt(mDataset.get(position).get("distance"))/1600);
        holder.distance.setText(String.valueOf(formattedValue)+" miles away");
        if(String.valueOf(mDataset.get(position).get("now")).equals("true")){
            holder.now.setText("Open");
            holder.now.setTextColor(Color.parseColor("#006400"));
        }else if(String.valueOf(mDataset.get(position).get("now")).equals("No Data")){
            holder.now.setText("--");
        }
        else{
            holder.now.setText("Closed");
            holder.now.setTextColor(Color.RED);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset==null){
            return 0;
        }
        return mDataset.size();
    }
}