package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Syukri on 12/26/17.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private String tabTitles[] = new String[] { "Home", "Feeds" };

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

    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View view= LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView textView= (TextView) view.findViewById(R.id.textView);
        textView.setText(getPageTitle(position));
        return view;
    }
}
