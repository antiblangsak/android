package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ListRSActivity extends AppCompatActivity {

    private ListView listViewRS;
    ArrayList<rumahSakitModel> RSmodel;
    public static listRSAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listViewRS = (ListView) findViewById( R.id.listViewRS );

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        RSmodel = new ArrayList<>();
        RSmodel.add(new rumahSakitModel("RS Cinere", "Cinere blok EF1/11", "14"));
        RSmodel.add(new rumahSakitModel("RS Bunda Margonda", "Margonda no 45 RT 2 RW 3", "16"));
        RSmodel.add(new rumahSakitModel("RS Ananda", "Margonda no 3", "17"));
        RSmodel.add(new rumahSakitModel("RS Damrah", "Jl. Bulungan no 9", "19"));
        RSmodel.add(new rumahSakitModel("RS Tiara Shella", "Jl. Hibrida no 36", "23"));
        RSmodel.add(new rumahSakitModel("RS Cipete", "Raffles Hills blok EF1/11", "24"));
        RSmodel.add(new rumahSakitModel("RS Citra Margonda", "Jl. Juanda no 45 RT 2 RW 33", "26"));
        RSmodel.add(new rumahSakitModel("RS Cipayung", "Cimanggis Depok", "37"));
        RSmodel.add(new rumahSakitModel("RS Cipto", "Jl. Raya Bogor no 53", "39"));
        RSmodel.add(new rumahSakitModel("RS Haji", "Jl. Akses UI no 23", "49"));
        adapter= new listRSAdapter(RSmodel,getApplicationContext());

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
