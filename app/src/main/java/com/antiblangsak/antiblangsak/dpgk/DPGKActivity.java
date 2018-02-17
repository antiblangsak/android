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
import com.antiblangsak.antiblangsak.common.BayarActivity;
import com.antiblangsak.antiblangsak.common.ClaimActivity;
import com.antiblangsak.antiblangsak.common.HistoryActivity;
import com.antiblangsak.antiblangsak.common.NasabahActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DPGKActivity extends AppCompatActivity {

    private LinearLayout rowDeskripsi;
    private LinearLayout rowNasabah;
    private LinearLayout rowBayar;
    private LinearLayout rowClaim;
    private LinearLayout rowHistory;

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
                startActivity(new Intent(DPGKActivity.this, DPGKDescriptionActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DPGK_SERVICE_ID_INTEGER));
            }
        });

        rowBayar = findViewById(R.id.rowBayar);
        rowBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DPGKActivity.this, BayarActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DPGK_SERVICE_ID_INTEGER));
            }
        });

        rowClaim = findViewById(R.id.rowKlaim);
        rowClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DPGKActivity.this, ClaimActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DPGK_SERVICE_ID_INTEGER));
            }
        });

        rowHistory = findViewById(R.id.rowHistory);
        rowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DPGKActivity.this, HistoryActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DPGK_SERVICE_ID_INTEGER));
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
