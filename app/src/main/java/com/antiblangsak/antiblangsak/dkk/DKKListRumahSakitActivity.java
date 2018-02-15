package com.antiblangsak.antiblangsak.dkk;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.adapters.RumahSakitAdapter;
import com.antiblangsak.antiblangsak.models.RumahSakitModel;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DKKListRumahSakitActivity extends AppCompatActivity {

    private ListView listViewRS;
    ArrayList<RumahSakitModel> RSmodel;
    public static RumahSakitAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dkklist_rumah_sakit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listViewRS = (ListView) findViewById( R.id.listViewRS );

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        RSmodel = new ArrayList<>();
        RSmodel.add(new RumahSakitModel("RS Cinere", "Cinere blok EF1/11", "14"));
        RSmodel.add(new RumahSakitModel("RS Bunda Margonda", "Margonda no 45 RT 2 RW 3", "16"));
        RSmodel.add(new RumahSakitModel("RS Ananda", "Margonda no 3", "17"));
        RSmodel.add(new RumahSakitModel("RS Damrah", "Jl. Bulungan no 9", "19"));
        RSmodel.add(new RumahSakitModel("RS Tiara Shella", "Jl. Hibrida no 36", "23"));
        RSmodel.add(new RumahSakitModel("RS Cipete", "Raffles Hills blok EF1/11", "24"));
        RSmodel.add(new RumahSakitModel("RS Citra Margonda", "Jl. Juanda no 45 RT 2 RW 33", "26"));
        RSmodel.add(new RumahSakitModel("RS Cipayung", "Cimanggis Depok", "37"));
        RSmodel.add(new RumahSakitModel("RS Cipto", "Jl. Raya Bogor no 53", "39"));
        RSmodel.add(new RumahSakitModel("RS Haji", "Jl. Akses UI no 23", "49"));
        adapter= new RumahSakitAdapter(RSmodel,getApplicationContext());

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
