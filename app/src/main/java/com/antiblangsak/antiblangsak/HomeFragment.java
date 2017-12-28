package com.antiblangsak.antiblangsak;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

    SharedPrefManager sharedPrefManager;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        tvLogout = view.findViewById(R.id.tvLogout);
        pbLogout = view.findViewById(R.id.pbLogout);
        btnRegisterAsNasabah = view.findViewById(R.id.btnRegisterAsNasabah);

        tvLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tvLogout.setVisibility(View.GONE);
                pbLogout.setVisibility(View.VISIBLE);

                String token = sharedPrefManager.getToken();
                String email = sharedPrefManager.getEmail();

                Call call = apiInterface.logout(token, email);
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
                        startActivity(new Intent(getActivity(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
                        startActivity(new Intent(getActivity(), LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        getActivity().finish();
                    }
                });
            }
        });

        btnRegisterAsNasabah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DaftarNasabahActivity.class));
            }
        });
        return view;
    }
}
