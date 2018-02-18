package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahKeluargaTerdaftarActivity extends AppCompatActivity {

    private Button btnDaftarkan;
    private EditText etKeluargaId;
    private EditText etNik;

    private String keluargaId;
    private String nik;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nasabah_keluarga_terdaftar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etKeluargaId = findViewById(R.id.etKeluargaId);
        etNik = findViewById(R.id.etNik);

        btnDaftarkan = (Button) findViewById(R.id.btnDaftarkan);
        btnDaftarkan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    Intent myIntent = new Intent(DaftarNasabahKeluargaTerdaftarActivity.this,
                            MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), AppConstant.GENERAL_MISSING_FIELD_ERROR_MESSAGE,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etKeluargaId.clearFocus();
                etNik.clearFocus();
                return true;
            }
        });
    }

    public boolean validateInput() {
        keluargaId = etKeluargaId.getText().toString();
        nik = etNik.getText().toString();

        boolean isValid = true;

        if(keluargaId.isEmpty()){
            etKeluargaId.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etKeluargaId.setError(null);
        }

        if(nik.isEmpty()){
            etNik.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etNik.setError(null);
        }

        return isValid;
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
