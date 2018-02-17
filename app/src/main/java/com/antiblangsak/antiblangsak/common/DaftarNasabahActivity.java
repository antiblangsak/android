package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.antiblangsak.antiblangsak.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahActivity extends AppCompatActivity {

    RelativeLayout btnKeluargaBaru;
    RelativeLayout btnKeluargaTerdaftar;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nasabah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnKeluargaBaru = (RelativeLayout) findViewById(R.id.btnDaftarAsNasabahKeluargaBaru);
        btnKeluargaTerdaftar = (RelativeLayout) findViewById(R.id.btnDaftarAsNasabahKeluargaTerdaftar);

        btnKeluargaBaru.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DaftarNasabahActivity.this,
                        DaftarNasabahKeluargaBaruActivity.class);
                startActivity(myIntent);
            }
        });

        btnKeluargaTerdaftar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DaftarNasabahActivity.this,
                        DaftarNasabahKeluargaTerdaftarActivity.class);
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
