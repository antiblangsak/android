package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ListRSActivity extends AppCompatActivity {

    private ListView listViewRS;
    ArrayList<RumahSakitModel> RSmodel;
    public static ListRSAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        ListView listViewRS = (ListView) findViewById( R.id.listViewRS );

        RSmodel = new ArrayList<>();
        RSmodel.add(new RumahSakitModel("Rumah Sakit Cinere", "Cinere blok EF1/11", "14"));
        RSmodel.add(new RumahSakitModel("Rumah Sakit Bunda", "Margonda no 45 RT 2 RW 3", "16"));
        RSmodel.add(new RumahSakitModel("Rumah Sakit Ananda", "Margonda no 3", "17"));
        adapter= new ListRSAdapter(RSmodel,getApplicationContext());

        listViewRS.setAdapter(adapter);
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
