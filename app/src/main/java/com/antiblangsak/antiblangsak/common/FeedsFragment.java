package com.antiblangsak.antiblangsak.common;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antiblangsak.antiblangsak.R;

/**
 * Created by Syukri on 12/26/17.
 */

public class FeedsFragment extends Fragment {

    Button btnTulisBerita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeds, container, false);


    }
}
