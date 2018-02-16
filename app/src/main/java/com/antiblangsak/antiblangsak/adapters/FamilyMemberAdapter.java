package com.antiblangsak.antiblangsak.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConfig;
import com.antiblangsak.antiblangsak.models.FamilyMemberModel;

import java.util.ArrayList;

/**
 * Created by Syukri on 2/16/18.
 */

public class FamilyMemberAdapter extends ArrayAdapter<FamilyMemberModel> {

    private ArrayList<FamilyMemberModel> dataSet;
    private Context context;

    private static class ViewHolder {
        TextView tvName;
        TextView tvRelation;
    }

    public FamilyMemberAdapter(ArrayList<FamilyMemberModel> dataSet, Context context) {
        super(context, R.layout.listitem_familymember, dataSet);
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FamilyMemberModel familyMemberModel = getItem(position);
        FamilyMemberAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new FamilyMemberAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listitem_familymember, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvRelation = convertView.findViewById(R.id.tvRelation);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FamilyMemberAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Typeface regularFont = Typeface.createFromAsset(context.getAssets(), AppConfig.REGULAR_FONT);
        Typeface boldFont = Typeface.createFromAsset(context.getAssets(), AppConfig.BOLD_FONT);

        viewHolder.tvName.setTypeface(boldFont);
        viewHolder.tvName.setText(familyMemberModel.getName());
        viewHolder.tvRelation.setTypeface(regularFont);
        viewHolder.tvRelation.setText(familyMemberModel.getRelation());

        // Return the completed view to render on screen
        return result;
    }
}
