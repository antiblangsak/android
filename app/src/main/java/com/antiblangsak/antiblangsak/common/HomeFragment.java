package com.antiblangsak.antiblangsak.common;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.dkk.DKKActivity;
import com.antiblangsak.antiblangsak.dpgk.DPGKActivity;
import com.antiblangsak.antiblangsak.dwk.DWKActivity;
import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

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

    CarouselView carouselView;

    int[] sampleImages = {R.drawable.dpgk, R.drawable.dkk, R.drawable.dwk};

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
            int familyStatus = sharedPrefManager.getFamilyStatus();
            if (familyStatus == 0) {
                btnRegisterAsNasabah.setText(R.string.home_datasedangdivalidasibutton);
                btnRegisterAsNasabah.setBackground(getResources().getDrawable(R.drawable.daftarnasabah_option));

                btnRegisterAsNasabah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Data Keluarga anda sedang dalam proses verifikasi, mohon tunggu beberapa saat.")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       // do thing
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        Button a = alert.getButton(DialogInterface.BUTTON_POSITIVE);

                        if(a != null) {
                            a.setTextColor(getResources().getColor(R.color.accepted));
                        }
                    }
                });

            } else {
                btnRegisterAsNasabah.setText(R.string.home_profilkeluargabutton);
                btnRegisterAsNasabah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), FamilyProfileActivity.class));
                    }
                });
            }
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

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
}