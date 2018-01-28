package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by martinovela on 1/28/18.
 */

public class ListRSAdapter extends ArrayAdapter<RumahSakitModel> {

    private ArrayList<RumahSakitModel> dataSet;
    Context mContext;



    // View lookup cache
    private static class ViewHolder {
        TextView list_tvNameRS;
        TextView list_tvAddress;
        TextView list_tvDistance;

    }

    public ListRSAdapter(ArrayList<RumahSakitModel> data, Context context) {
        super(context, R.layout.content_list_rs, data);
        this.dataSet = data;
        this.mContext=context;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RumahSakitModel RumahSakitModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.content_list_rs, parent, false);
            viewHolder.list_tvNameRS = (TextView) convertView.findViewById(R.id.list_tvNameRS);
            viewHolder.list_tvAddress = (TextView) convertView.findViewById(R.id.list_tvAddress);
            viewHolder.list_tvDistance = (TextView) convertView.findViewById(R.id.list_tvDistance);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Typeface customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Comfortaa-Bold.ttf");

        viewHolder.list_tvNameRS.setTypeface(customFont);
        viewHolder.list_tvAddress.setTypeface(customFont);
        viewHolder.list_tvDistance.setTypeface(customFont);

        viewHolder.list_tvNameRS.setText(RumahSakitModel.getName());
        viewHolder.list_tvAddress.setText(RumahSakitModel.getAddress());
        viewHolder.list_tvDistance.setText(RumahSakitModel.getDistance()+" km");

        // Return the completed view to render on screen
        return convertView;
    }
}
