package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
    private TextView tvSyaratKetentuan;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deskripsi_dkk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        sharedPrefManager = new SharedPrefManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnRS = (Button) findViewById(R.id.btnRS);
        tvDeskripsiDKK = (TextView) findViewById(R.id.tvDeskripsiDKK);
        tvSyaratKetentuan = (TextView) findViewById(R.id.tvSyaratKetentuan);

        btnRS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeskripsiDKKActivity.this, ListRSActivity.class));
            }
        });
        tvSyaratKetentuan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeskripsiDKKActivity.this, KetentuanDKKActivity.class));
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
