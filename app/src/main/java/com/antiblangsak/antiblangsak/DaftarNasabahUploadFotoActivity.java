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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahUploadFotoActivity extends AppCompatActivity {

    private EditText etPhotoKtp;
    private EditText etPhotoKk;

    private PhotoView imPhotoKtp;
    private PhotoView imPhotoKk;
    private CheckBox chAgree;
    private Button btnDaftarkanKeluarga;

    private String photoKtp;
    private String photoKk;

    private String DEFAULT_PHOTO_NAME;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_nasabah_upload_foto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.daftarnasabah_title);

        DEFAULT_PHOTO_NAME = getResources().getString(R.string.daftarrekening_accountphoto);

        etPhotoKtp = findViewById(R.id.etPhotoKtp);
        etPhotoKk = findViewById(R.id.etPhotoKk);
        imPhotoKtp = findViewById(R.id.imPhotoKtp);
        imPhotoKk = findViewById(R.id.imPhotoKk);
        chAgree = findViewById(R.id.chAgree);

        etPhotoKtp.setFocusable(false);
        etPhotoKtp.setClickable(true);
        etPhotoKk.setFocusable(false);
        etPhotoKk.setClickable(true);

        etPhotoKtp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent();
                openGalleryIntent.setType("image/*");
                openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),
                        AppConstant.UPLOAD_FOTO_KTP_REQUEST_GALLERY);
            }
        });

        etPhotoKk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent();
                openGalleryIntent.setType("image/*");
                openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),
                        AppConstant.UPLOAD_FOTO_KK_REQUEST_GALLERY);
            }
        });


        btnDaftarkanKeluarga = (Button) findViewById(R.id.btnDaftarkanKeluarga);
        btnDaftarkanKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    Intent myIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                            MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), AppConstant.GENERAL_MISSING_FIELD_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etPhotoKtp.clearFocus();
                etPhotoKk.clearFocus();
                return true;
            }
        });
    }

    public boolean validateInput() {
        photoKtp = etPhotoKtp.getText().toString();
        photoKk = etPhotoKk.getText().toString();

        boolean isValid = true;

        if(photoKtp.equals(DEFAULT_PHOTO_NAME)){
            etPhotoKtp.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etPhotoKtp.setError(null);
        }

        if(photoKk.equals(DEFAULT_PHOTO_NAME)){
            etPhotoKk.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etPhotoKk.setError(null);
        }

        if(!chAgree.isChecked()) {
            chAgree.setError(AppConstant.GENERAL_CHECKBOX_ERROR_MESSAGE);
            isValid = false;
        } else {
            chAgree.setError(null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                InputStream is = getContentResolver().openInputStream(imageUri);
                Log.v("INPUT STREAM", "Image size: " + is.available());
                byte[] imageBytes = AppHelper.getBytes(is);
//                uploadImage(imageBytes);

                if (requestCode == AppConstant.UPLOAD_FOTO_KTP_REQUEST_GALLERY) {
                    AppHelper.showImageThumbnail(imageUri, this, imPhotoKtp, etPhotoKtp);
                    etPhotoKtp.setText(AppHelper.getFileName(this, imageUri));
                    etPhotoKtp.setError(null);

                    imPhotoKtp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent fullScreenIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                                    FullScreenImageActivity.class);
                            fullScreenIntent.setData(imageUri);
                            startActivity(fullScreenIntent);
                        }
                    });
                } else {
                    AppHelper.showImageThumbnail(imageUri, this, imPhotoKk, etPhotoKk);
                    etPhotoKk.setText(AppHelper.getFileName(this, imageUri));
                    etPhotoKk.setError(null);

                    imPhotoKk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent fullScreenIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                                    FullScreenImageActivity.class);
                            fullScreenIntent.setData(imageUri);
                            startActivity(fullScreenIntent);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
