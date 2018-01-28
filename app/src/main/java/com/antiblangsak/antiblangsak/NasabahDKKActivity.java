package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NasabahDKKActivity extends AppCompatActivity {

    private ListView nasabahListView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah_dkk);

        nasabahListView = findViewById(R.id.list_view_nasabah);

    }
}
