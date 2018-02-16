package com.antiblangsak.antiblangsak.dwk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.antiblangsak.antiblangsak.common.FamilyProfileActivity;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.common.NasabahActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DWKActivity extends AppCompatActivity {

    private LinearLayout rowDeskripsi;
    private LinearLayout rowHistory;
    private LinearLayout rowNasabah;

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPrefManager = new SharedPrefManager(this);

        rowDeskripsi = findViewById(R.id.rowDeskripsi);
        rowHistory = findViewById(R.id.rowHistory);
        rowNasabah = findViewById(R.id.rowNasabah);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));

        rowDeskripsi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DWKActivity.this, DWKDescriptionActivity.class));
            }
        });

        rowNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DWKActivity.this, NasabahActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DWK_SERVICE_ID_INTEGER));
            }
        });

        rowHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

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
