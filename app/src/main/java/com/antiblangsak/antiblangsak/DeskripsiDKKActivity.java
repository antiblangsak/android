package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DeskripsiDKKActivity extends AppCompatActivity {

    private Button btnRS;
    private TextView tvDeskripsiDKK;
    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_dkk);

        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnRS = (Button) findViewById(R.id.btnRS);
        tvDeskripsiDKK = (TextView) findViewById(R.id.tvDeskripsiDKK);

        btnRS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeskripsiDKKActivity.this, ListRSActivity.class));
            }
        });

    }
}
