package com.antiblangsak.antiblangsak.dwk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.antiblangsak.antiblangsak.common.BayarActivity;
import com.antiblangsak.antiblangsak.common.ClaimActivity;
import com.antiblangsak.antiblangsak.common.DaftarNasabahActivity;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.common.HistoryActivity;
import com.antiblangsak.antiblangsak.common.NasabahActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DWKActivity extends AppCompatActivity {

    private LinearLayout rowDeskripsi;
    private LinearLayout rowHistory;
    private LinearLayout rowNasabah;
    private LinearLayout rowBayar;
    private LinearLayout rowClaim;

    private SharedPrefManager sharedPrefManager;
    private boolean hasFamily;

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

        hasFamily = sharedPrefManager.hasFamily();

        rowDeskripsi = findViewById(R.id.rowDeskripsi);
        rowNasabah = findViewById(R.id.rowNasabah);
        rowBayar = findViewById(R.id.rowBayar);
        rowClaim = findViewById(R.id.rowKlaim);
        rowHistory = findViewById(R.id.rowHistory);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));

        rowDeskripsi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DWKActivity.this, DWKDescriptionActivity.class));
            }
        });


        if (hasFamily) {

            rowNasabah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DWKActivity.this, NasabahActivity.class)
                            .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DWK_SERVICE_ID_INTEGER));
                }
            });

            rowBayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DWKActivity.this, BayarActivity.class)
                            .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DWK_SERVICE_ID_INTEGER));
                }
            });

            rowClaim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DWKActivity.this, ClaimActivity.class)
                            .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DWK_SERVICE_ID_INTEGER));
                }
            });

            rowHistory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DWKActivity.this, HistoryActivity.class)
                            .putExtra(AppConstant.KEY_SERVICE_ID, AppConstant.DWK_SERVICE_ID_INTEGER));
                }
            });

        } else {

            rowNasabah.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DWKActivity.this);
                    builder.setMessage("Harap mendaftarkan keluarga anda terlebih dahulu sebelum menggunakan layanan ini.")

                            .setCancelable(true)
                            .setPositiveButton("Daftar Sekarang", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(DWKActivity.this, DaftarNasabahActivity.class));
                                }
                            })
                            .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    Button a = alert.getButton(DialogInterface.BUTTON_POSITIVE);

                    Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);

                    if(a != null) {
                        a.setTextColor(getResources().getColor(R.color.accepted));
                    }

                    if(b != null) {
                        b.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            });

            rowBayar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DWKActivity.this);
                    builder.setMessage("Harap mendaftarkan keluarga anda terlebih dahulu sebelum menggunakan layanan ini.")

                            .setCancelable(true)
                            .setPositiveButton("Daftar Sekarang", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(DWKActivity.this, DaftarNasabahActivity.class));
                                }
                            })
                            .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    Button a = alert.getButton(DialogInterface.BUTTON_POSITIVE);

                    Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);

                    if(a != null) {
                        a.setTextColor(getResources().getColor(R.color.accepted));
                    }

                    if(b != null) {
                        b.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            });

            rowClaim.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DWKActivity.this);
                    builder.setMessage("Harap mendaftarkan keluarga anda terlebih dahulu sebelum menggunakan layanan ini.")

                            .setCancelable(true)
                            .setPositiveButton("Daftar Sekarang", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(DWKActivity.this, DaftarNasabahActivity.class));
                                }
                            })
                            .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    Button a = alert.getButton(DialogInterface.BUTTON_POSITIVE);

                    Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);

                    if(a != null) {
                        a.setTextColor(getResources().getColor(R.color.accepted));
                    }

                    if(b != null) {
                        b.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            });

            rowHistory.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DWKActivity.this);
                    builder.setMessage("Harap mendaftarkan keluarga anda terlebih dahulu sebelum menggunakan layanan ini.")

                            .setCancelable(true)
                            .setPositiveButton("Daftar Sekarang", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(DWKActivity.this, DaftarNasabahActivity.class));
                                }
                            })
                            .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    Button a = alert.getButton(DialogInterface.BUTTON_POSITIVE);

                    Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);

                    if(a != null) {
                        a.setTextColor(getResources().getColor(R.color.accepted));
                    }

                    if(b != null) {
                        b.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            });

        }
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
