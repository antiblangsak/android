package com.antiblangsak.antiblangsak.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConfig;
import com.antiblangsak.antiblangsak.models.NasabahModel;

import java.util.ArrayList;

/**
 * Created by Syukri on 1/28/18.
 */

public class NasabahAdapter extends ArrayAdapter<NasabahModel> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<NasabahModel> mDataSource;

    private boolean isRegistered;

    private static class ViewHolder {
        TextView tvName;
        TextView tvDesc;
    }

    public NasabahAdapter(ArrayList<NasabahModel> mDataSource, Context mContext, boolean isRegistered) {
        super(mContext, R.layout.listitem_nasabah, mDataSource);
        this.mDataSource = mDataSource;
        this.mContext = mContext;
        this.isRegistered = isRegistered;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NasabahModel model = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.listitem_nasabah, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvDesc = convertView.findViewById(R.id.tvDesc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Typeface customFont = Typeface.createFromAsset(mContext.getAssets(), AppConfig.REGULAR_FONT);

        viewHolder.tvName.setTypeface(customFont);
        viewHolder.tvDesc.setTypeface(customFont);

        viewHolder.tvName.setText(model.getName());
        if (isRegistered) {
            viewHolder.tvDesc.setText(model.getStatus());
        } else {
            viewHolder.tvDesc.setText(model.getRelation());
        }
        return convertView;
    }
}
