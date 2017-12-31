package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarRekeningActivity extends AppCompatActivity {

    private EditText etBranchName;
    private EditText etAccountNumber;
    private EditText etAccountHolderName;
    private EditText etAccountPhoto;
    private Button btnDaftarkanRekening;
    private PhotoView imAccountPhoto;
    private final String[] BANK_NAMES = new String[]{
            "Pilih Nama Bank...",
            "BNI",
            "BRI",
            "Bank Mandiri",
            "BCA",
            "Bank Permata",
            "Bank Bukopin"
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_rekening);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.daftarnasabah_title);

        etBranchName = (EditText) findViewById(R.id.etBranchName);
        etAccountNumber = (EditText) findViewById(R.id.etAccountNumber);
        etAccountHolderName = (EditText) findViewById(R.id.etAccountHolderName);
        etAccountPhoto = (EditText) findViewById(R.id.etAccountPhoto);
        etAccountPhoto.setFocusable(false);
        etAccountPhoto.setClickable(true);
        imAccountPhoto = (PhotoView) findViewById(R.id.imAccountPhoto);

        Spinner spinner = (Spinner) findViewById(R.id.bankNameSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.item_spinner, BANK_NAMES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setPadding(0, 8, 0, 8);
                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner. First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.gray));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };

        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etAccountPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("etAccountPhoto", "CLICKED");
                Intent openGalleryIntent = new Intent();
                openGalleryIntent.setType("image/*");
                openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),
                        AppConstant.DAFTAR_REKENING_REQUEST_GALLERY);
            }
        });

        btnDaftarkanRekening = (Button) findViewById(R.id.btnDaftarkanRekening);
        btnDaftarkanRekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DaftarRekeningActivity.this,
                        DaftarNasabahUploadFotoActivity.class);
                startActivity(myIntent);
            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etBranchName.clearFocus();
                etAccountNumber.clearFocus();
                etAccountHolderName.clearFocus();
                etAccountPhoto.clearFocus();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.DAFTAR_REKENING_REQUEST_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                InputStream is = getContentResolver().openInputStream(imageUri);
                Log.v("INPUT STREAM", "Image size: " + is.available());

                AppHelper.showImageThumbnail(imageUri, this, imAccountPhoto, etAccountPhoto);
                etAccountPhoto.setText(AppHelper.getFileName(this, imageUri));

                byte[] imageBytes = AppHelper.getBytes(is);
                uploadImage(imageBytes);

                imAccountPhoto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent fullScreenIntent = new Intent(DaftarRekeningActivity.this,
                                FullScreenImageActivity.class);
                        fullScreenIntent.setData(imageUri);
                        startActivity(fullScreenIntent);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void uploadImage(byte[] imageBytes) {
        Log.v("IMAGE_BYTES", Arrays.toString(imageBytes));
    }
}
