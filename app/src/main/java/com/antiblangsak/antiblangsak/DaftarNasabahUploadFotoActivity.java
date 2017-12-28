package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahUploadFotoActivity extends AppCompatActivity {

    private EditText etPhotoKTP;
    private EditText etPhotoKK;
    private Button btnDaftarkanKeluarga;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nasabah_upload_foto);

        etPhotoKTP = (EditText) findViewById(R.id.etPhotoKTP);
        etPhotoKK = (EditText) findViewById(R.id.etPhotoKK);

        btnDaftarkanKeluarga = (Button) findViewById(R.id.btnDaftarkanKeluarga);
        btnDaftarkanKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                        MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etPhotoKTP.clearFocus();
                etPhotoKK.clearFocus();
                return true;
            }
        });
    }
}
