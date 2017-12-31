package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahUploadFotoActivity extends AppCompatActivity {

    private EditText etPhotoKtp;
    private EditText etPhotoKk;
    private PhotoView imPhotoKtp;
    private PhotoView imPhotoKk;
    private Button btnDaftarkanKeluarga;

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

        etPhotoKtp = (EditText) findViewById(R.id.etPhotoKtp);
        etPhotoKk = (EditText) findViewById(R.id.etPhotoKk);
        imPhotoKtp = (PhotoView) findViewById(R.id.imPhotoKtp);
        imPhotoKk = (PhotoView) findViewById(R.id.imPhotoKk);

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
                etPhotoKtp.clearFocus();
                etPhotoKk.clearFocus();
                return true;
            }
        });
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
