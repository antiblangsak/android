package com.antiblangsak.antiblangsak.dpgk;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.common.NasabahActivity;
import com.antiblangsak.antiblangsak.dkk.DKKActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DPGKActivity extends AppCompatActivity {

    private LinearLayout rowDeskripsi;
    private LinearLayout rowNasabah;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpgk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dpgk_color)));

        rowNasabah = findViewById(R.id.rowNasabah);
        rowNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DPGKActivity.this, NasabahActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DPGK_SERVICE_ID_INTEGER));
            }
        });

        rowDeskripsi = findViewById(R.id.rowDeskripsi);
        rowDeskripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DPGKActivity.this, DPGKDescriptionActivity.class);
                startActivity(myIntent);
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
