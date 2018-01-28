package com.antiblangsak.antiblangsak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListRSActivity extends AppCompatActivity {

    private ListView listViewRS;
    ArrayList<rumahSakitModel> RSmodel;
    public static listRSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rs);


        ListView listViewRS = (ListView) findViewById( R.id.listViewRS );



        RSmodel = new ArrayList<>();
        RSmodel.add(new rumahSakitModel("Rumah Sakit Cinere", "Cinere blok EF1/11", "14"));
        RSmodel.add(new rumahSakitModel("Rumah Sakit Bunda", "Margonda no 45 RT 2 RW 3", "16"));
        RSmodel.add(new rumahSakitModel("Rumah Sakit Ananda", "Margonda no 3", "17"));
        adapter= new listRSAdapter(RSmodel,getApplicationContext());

        listViewRS.setAdapter(adapter);



    }
}
