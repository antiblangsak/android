package com.antiblangsak.antiblangsak;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Syukri on 12/26/17.
 */

public class HomeFragment extends Fragment {

    TextView tvLogout;
    ProgressBar pbLogout;
    Button btnRegisterAsNasabah;
    RelativeLayout btnServiceDpgk;
    RelativeLayout btnServiceDkk;
    RelativeLayout btnServiceDwk;

    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;

    private String token;
    private String email;
    private boolean hasFamily;

    int familyId = -1;
    int familyStatus = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (sharedPrefManager.hasFamily()) {
            familyId = sharedPrefManager.getFamilyId();
            familyStatus = sharedPrefManager.getFamilyStatus();
        }

        tvLogout = view.findViewById(R.id.tvLogout);
        pbLogout = view.findViewById(R.id.pbLogout);
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
                startActivity(new Intent(getActivity(), DKKActivity.class)
                        .putExtra("FAMILY_ID", familyId)
                        .putExtra("FAMILY_STATUS", familyStatus));
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