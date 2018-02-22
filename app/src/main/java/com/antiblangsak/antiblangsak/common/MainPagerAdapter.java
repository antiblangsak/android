package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.R;

/**
 * Created by Syukri on 12/26/17.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private String tabTitles[] = new String[] { "HOME", "FEEDS" };

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tabHome = new HomeFragment();
                return tabHome;
            case 1:
                FeedsFragment tabFeeds = new FeedsFragment();
                return tabFeeds;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public String getPageTitle(int position) {
        return tabTitles[position];
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/view_customtabmtab.xml` with a TextView and ImageView
        View view = LayoutInflater.from(context).inflate(R.layout.view_customtab, null);
        TextView textView= (TextView) view.findViewById(R.id.textView);
        textView.setText(getPageTitle(position));
        return view;
    }
}
