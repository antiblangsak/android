package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Alica on 1/28/18.
 */

public class DKKHistoryAdapter extends ArrayAdapter<HistoryModel> {
    private ArrayList<HistoryModel> dataSet;
    private Context context;

    private static class ViewHolder {
        ImageView imType;
        TextView tvType;
        TextView tvCreatedAt;
        TextView tvStatus;
    }

    public DKKHistoryAdapter(ArrayList<HistoryModel> dataSet, Context context) {
        super(context, R.layout.listview_dkkhistory, dataSet);
        this.dataSet = dataSet;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryModel historyModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_dkkhistory, parent, false);
            viewHolder.imType = convertView.findViewById(R.id.list_icon_dkk_history);
            viewHolder.tvType = convertView.findViewById(R.id.list_tvTitle);
            viewHolder.tvStatus = convertView.findViewById(R.id.list_tvStatus);
            viewHolder.tvCreatedAt = convertView.findViewById(R.id.list_tvTanggal);


            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Typeface regularFont = Typeface.createFromAsset(context.getAssets(), "fonts/Comfortaa-Regular.ttf");
        Typeface boldFont = Typeface.createFromAsset(context.getAssets(), "fonts/Comfortaa-Bold.ttf");
        if (historyModel.getType().equals("Pembayaran")) {
            viewHolder.imType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bayar));
        } else {
            viewHolder.imType.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_klaim));
        }
        viewHolder.tvType.setTypeface(boldFont);
        viewHolder.tvType.setText(historyModel.getType());
        viewHolder.tvStatus.setTypeface(boldFont);
        viewHolder.tvStatus.setText(historyModel.getStatus());
        viewHolder.tvStatus.setTextColor(context.getResources().getColor(historyModel.getStatusColor()));
        viewHolder.tvCreatedAt.setTypeface(regularFont);
        viewHolder.tvCreatedAt.setText(historyModel.getCreatedAt());

        // Return the completed view to render on screen
        return result;
    }
}
