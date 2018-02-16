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

import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.common.HistoryActivity;
import com.antiblangsak.antiblangsak.common.ProfilKeluargaActivity;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DWKActivity extends AppCompatActivity {
    private LinearLayout rowDeskripsiDWK;
    private LinearLayout rowHistoryDWK;
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

        rowDeskripsiDWK = (LinearLayout) findViewById(R.id.rowDeskripsi);
        rowHistoryDWK = (LinearLayout) findViewById(R.id.rowHistory);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));

        rowDeskripsiDWK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DWKActivity.this, DWKDescriptionActivity.class));
            }
        });

        rowHistoryDWK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DWKActivity.this, HistoryActivity.class)
                        .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DWK_SERVICE_ID_INTEGER));
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
