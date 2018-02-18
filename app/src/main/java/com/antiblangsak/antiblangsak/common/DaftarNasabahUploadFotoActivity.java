package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.antiblangsak.antiblangsak.utils.ImageUtil;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.AppHelper;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.mvc.imagepicker.ImagePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarNasabahUploadFotoActivity extends AppCompatActivity {

    private EditText etPhotoKtp;
    private EditText etPhotoKk;

    private PhotoView imPhotoKtp;
    private PhotoView imPhotoKk;
    private CheckBox chAgree;
    private Button btnDaftarkanKeluarga;
    private ProgressBar progressBar;

    private Intent previousIntent;
    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String photoKtp;
    private String photoKtpBase64;
    private String photoKk;
    private String photoKkBase64;

    private String DEFAULT_PHOTO_NAME;

    private String token;
    private String emailUser;
    private int userId;

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

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        userId = sharedPrefManager.getId();

        previousIntent = getIntent();

        DEFAULT_PHOTO_NAME = getResources().getString(R.string.daftarrekening_accountphoto);

        etPhotoKtp = findViewById(R.id.etPhotoKtp);
        etPhotoKk = findViewById(R.id.etPhotoKk);
        imPhotoKtp = findViewById(R.id.imPhotoKtp);
        imPhotoKk = findViewById(R.id.imPhotoKk);
        chAgree = findViewById(R.id.chAgree);
        progressBar = findViewById(R.id.pbDaftarkan);

        etPhotoKtp.setFocusable(false);
        etPhotoKtp.setClickable(true);
        etPhotoKk.setFocusable(false);
        etPhotoKk.setClickable(true);

        etPhotoKtp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(DaftarNasabahUploadFotoActivity.this, "Select your image:",
                        AppConstant.UPLOAD_FOTO_KTP_REQUEST_GALLERY, true);
            }
        });

        etPhotoKk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(DaftarNasabahUploadFotoActivity.this, "Select your image:",
                        AppConstant.UPLOAD_FOTO_KK_REQUEST_GALLERY, true);
            }
        });


        btnDaftarkanKeluarga = (Button) findViewById(R.id.btnDaftarkanKeluarga);
        btnDaftarkanKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    btnDaftarkanKeluarga.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    Call call = apiInterface.registerFamilyUploadPhotos(token, userId, photoKkBase64, photoKtpBase64);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            JSONObject body = null;
                            int statusCode = response.code();
                            Log.w("status", "status: " + statusCode);

                            if (statusCode == 201) {
                                try {
                                    body = new JSONObject(new Gson().toJson(response.body()));
                                    Log.w("RESPONSE", "body: " + body.toString());

                                    JSONObject data = body.getJSONObject("data");
                                    int familyId = data.getInt("id");
                                    int familyStatus = data.getInt("status");

                                    sharedPrefManager.saveBoolean(SharedPrefManager.HAS_FAMILY, true);
                                    sharedPrefManager.saveInt(SharedPrefManager.FAMILY_ID, familyId);
                                    sharedPrefManager.saveInt(SharedPrefManager.FAMILY_STATUS, familyStatus);

                                    Intent myIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                                            MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    sharedPrefManager.clearPhotos();
                                    startActivity(myIntent);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                try {
                                    Log.w("body", response.errorBody().string());
                                    sharedPrefManager.logout();
                                    Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DaftarNasabahUploadFotoActivity.this, LoginActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();

                                    Call callLogout = apiInterface.logout(token, emailUser);
                                    callLogout.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {

                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                            btnDaftarkanKeluarga.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
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
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.v("MASUK IMAGE PICKER", "");
            final Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            final String base64 = ImageUtil.convert(ImageUtil.compress(bitmap));

            if (requestCode == AppConstant.UPLOAD_FOTO_KTP_REQUEST_GALLERY) {
                AppHelper.showImageWithGlide(this, bitmap, imPhotoKtp);
                etPhotoKtp.setText(getResources().getString(R.string.daftarrekening_accountphotoubah));
                etPhotoKtp.setError(null);
                photoKtpBase64 = base64;

                imPhotoKtp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent fullScreenIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                                FullScreenImageActivity.class);
                        fullScreenIntent.putExtra("TYPE", SharedPrefManager.KTP_PHOTO_BASE64);
                        sharedPrefManager.saveString(SharedPrefManager.KTP_PHOTO_BASE64, photoKkBase64);
                        startActivity(fullScreenIntent);
                    }
                });
            } else {
                AppHelper.showImageWithGlide(this, bitmap, imPhotoKk);
                etPhotoKk.setText(getResources().getString(R.string.daftarrekening_accountphotoubah));
                etPhotoKk.setError(null);
                photoKkBase64 = base64;

                imPhotoKk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent fullScreenIntent = new Intent(DaftarNasabahUploadFotoActivity.this,
                                FullScreenImageActivity.class);
                        fullScreenIntent.putExtra("TYPE", SharedPrefManager.KK_PHOTO_BASE64);
                        sharedPrefManager.saveString(SharedPrefManager.KK_PHOTO_BASE64, photoKkBase64);
                        startActivity(fullScreenIntent);
                    }
                });
            }
        }
    }
}
