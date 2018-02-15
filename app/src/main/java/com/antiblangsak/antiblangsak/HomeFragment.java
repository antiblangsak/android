package com.antiblangsak.antiblangsak;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;

/**
 * Created by Syukri on 12/26/17.
 */

public class HomeFragment extends Fragment {

    private Button btnRegisterAsNasabah;
    private RelativeLayout btnServiceDpgk;
    private RelativeLayout btnServiceDkk;
    private RelativeLayout btnServiceDwk;

    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;

    private String token;
    private String email;
    private boolean hasFamily;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnRegisterAsNasabah = view.findViewById(R.id.btnRegisterAsNasabah);
        btnServiceDpgk = view.findViewById(R.id.btnServiceDpgk);
        btnServiceDkk = view.findViewById(R.id.btnServiceDkk);
        btnServiceDwk = view.findViewById(R.id.btnServiceDwk);

        token = sharedPrefManager.getToken();
        email = sharedPrefManager.getEmail();
        hasFamily = sharedPrefManager.hasFamily();

        if (hasFamily) {
            btnRegisterAsNasabah.setText(R.string.home_profilkeluargabutton);
        } else {
            btnRegisterAsNasabah.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), DaftarNasabahActivity.class));
                }
            });
        }

        btnServiceDpgk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DPGKActivity.class));
            }
        });

        btnServiceDkk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DKKActivity.class));
            }
        });

        btnServiceDwk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DWKActivity.class));
            }
        });

        return view;
    }
}
