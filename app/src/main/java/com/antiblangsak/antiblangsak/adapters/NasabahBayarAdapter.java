package com.antiblangsak.antiblangsak.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.antiblangsak.antiblangsak.models.NasabahBayarModel;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.common.BayarActivity;

import java.util.ArrayList;

/**
 * Created by citra on 2/11/2018.
 */

public class NasabahBayarAdapter extends ArrayAdapter<NasabahBayarModel>{
    private Context mContext;
    private ArrayList<NasabahBayarModel> mDataSource;

    private static class ViewHolder {
        CheckBox tvName;
    }

    public NasabahBayarAdapter(ArrayList<NasabahBayarModel> mDataSource, Context mContext) {
        super(mContext, R.layout.listitem_nasabahbayar, mDataSource);
        this.mDataSource = mDataSource;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NasabahBayarModel model = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.listitem_nasabahbayar, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.nasabahName);
//            CheckBox tvName = (CheckBox) convertView.findViewById(R.id.nasabahName);
//            viewHolder.tvName.setText(model.getName());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Typeface customFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Comfortaa-Bold.ttf");

        viewHolder.tvName.setTypeface(customFont);

        viewHolder.tvName.setText(model.getName());
//        if (isChecked) {
//            viewHolder.tvDesc.setText(model.getStatus());
//        } else {
//            viewHolder.tvDesc.setText(model.getRelation());
//        }
        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("PRESSED", model.getName());
                model.checked();

                if (model.isChecked()) {
                    BayarActivity.addTagihan();
                } else {
                    BayarActivity.reduceTagihan();
                }
                BayarActivity.showTagihan();
            }
        });
        return convertView;
    }
}
