package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.riyagayasen.easyaccordion.AccordionView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfilKeluargaActivity extends AppCompatActivity {

    AccordionView accordionView ;
    LinearLayout ll1;
    LinearLayout ll2;
    LinearLayout ll3;
    LinearLayout ll4;
    LinearLayout ll5;
    LinearLayout ll6;
    LinearLayout ll7;
    LinearLayout ll8;
    LinearLayout ll9;


    public boolean isClicked = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_keluarga);

        accordionView = (AccordionView) findViewById(R.id.accDataKeluarga);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        ll5 = (LinearLayout) findViewById(R.id.ll5);
        ll6 = (LinearLayout) findViewById(R.id.ll6);
        ll7 = (LinearLayout) findViewById(R.id.ll7);
        ll8 = (LinearLayout) findViewById(R.id.ll8);
        ll9 = (LinearLayout) findViewById(R.id.ll9);


        accordionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked){
                    isClicked = false;
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                    ll4.setVisibility(View.VISIBLE);
                    ll5.setVisibility(View.VISIBLE);
                    ll6.setVisibility(View.VISIBLE);
                    ll7.setVisibility(View.VISIBLE);
                    ll8.setVisibility(View.VISIBLE);
                    ll9.setVisibility(View.VISIBLE);
                }
                else{
                    isClicked = true;
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                    ll4.setVisibility(View.GONE);
                    ll5.setVisibility(View.GONE);
                    ll6.setVisibility(View.GONE);
                    ll7.setVisibility(View.GONE);
                    ll8.setVisibility(View.GONE);
                    ll9.setVisibility(View.GONE);
                }


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
