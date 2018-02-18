package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.AppHelper;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.models.NasabahModel;
import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.antiblangsak.antiblangsak.utils.ImageUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.mvc.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ClaimActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String token;
    private String emailUser;
    private int userId;
    private int familyId;

    private int serviceId;

    private Spinner spinner;
    private TextView tvRemainingAmountText;
    private TextView tvRemainingAmount;
    private EditText etPhoto;
    private PhotoView imPhoto;
    private Button button;
    private TextView tvNote;

    private LinearLayout mainLayout;
    private ProgressBar progressBar;
    private ProgressBar progressBarKirim;

    private final String DEFAULT_CLIENT_NAME = "Pilih Nasabah...";
    private String DEFAULT_PHOTO_NAME;
    private String[] clientNames;
    private NasabahModel[] clientModels;
    private String photoBase64;
    private String clientName;

    private NasabahModel selectedModel;
    private int selectedClientId = -1;
    private boolean claimDwkAvailable = true;

    private Call callGetInfo;
    private Call callPost;

    private int defaultTextColor;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DEFAULT_PHOTO_NAME = getResources().getString(R.string.klaim_claimphoto);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        userId = sharedPrefManager.getId();
        familyId = sharedPrefManager.getFamilyId();

        serviceId = getIntent().getIntExtra(AppConstant.KEY_SERVICE_ID, -1);

        if (serviceId == AppConstant.DPGK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dpgk_color)));
            callGetInfo = apiInterface.getDPGKClaimInfo(token, familyId);
        } else if (serviceId == AppConstant.DWK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));
            callGetInfo = apiInterface.getDWKClaimInfo(token, familyId);
        } else {
            Toast.makeText(getApplicationContext(), "Invalid service", Toast.LENGTH_SHORT).show();
            finish();
        }

        spinner = findViewById(R.id.spinner);
        tvRemainingAmountText = findViewById(R.id.tvRemainingAmountText);
        tvRemainingAmount = findViewById(R.id.tvRemainingAmount);
        etPhoto = findViewById(R.id.etPhoto);
        imPhoto = findViewById(R.id.imPhoto);
        button = findViewById(R.id.button);
        tvNote = findViewById(R.id.note);

        defaultTextColor = tvNote.getTextColors().getDefaultColor();

        mainLayout = findViewById(R.id.mainLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBarKirim = findViewById(R.id.progressBarKirim);

        etPhoto.setFocusable(false);
        etPhoto.setClickable(true);

        etPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(ClaimActivity.this, "Select your image:",
                        AppConstant.UPLOAD_FOTO_KLAIM, true);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateInput()) {
                    if(!claimDwkAvailable) {
                        Toast.makeText(getApplicationContext(), "Tidak dapat melakukan claim untuk nasabah ini", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), AppConstant.GENERAL_MISSING_FIELD_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    button.setVisibility(View.GONE);
                    progressBarKirim.setVisibility(View.VISIBLE);
                    
                    Log.w("CLIENT ID", selectedClientId + "");
                    callPost = apiInterface.postClaim(token, serviceId, userId, selectedClientId);
                    callPost.enqueue(new Callback() {

                        @Override
                        public void onResponse(Call call, Response response) {
                            JSONObject body = null;
                            int statusCode = response.code();
                            Log.w("status", "status: " + statusCode);

                            if (statusCode == AppConstant.HTTP_RESPONSE_201_CREATED) {
                                try {
                                    body = new JSONObject(new Gson().toJson(response.body()));
                                    Log.w("RESPONSE", "body: " + body.toString());

                                    JSONObject data = body.getJSONObject("data");
                                    int claimId = data.getInt("id");

                                    startActivity(new Intent(ClaimActivity.this, HistoryClaimActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra(AppConstant.KEY_SERVICE_ID, serviceId)
                                            .putExtra("claimId", claimId));

                                    Call uploadImage = apiInterface.uploadClaimFile(token, claimId, sharedPrefManager.getPhoto(SharedPrefManager.CLAIM_PHOTO_BASE64));
                                    uploadImage.enqueue(new Callback() {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            try {
                                                int statusCode = response.code();
                                                Log.w("STATUS", "status: " + statusCode);

                                                JSONObject body = new JSONObject(new Gson().toJson(response.body()));
                                                Log.w("RESPONSE", "body: " + body.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "Upload image failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else if (statusCode == AppConstant.HTTP_RESPONSE_401_UNAUTHORIZED) {
                                Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                                AppHelper.performLogout(response, sharedPrefManager, ClaimActivity.this, apiInterface);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), AppConstant.API_CALL_UNKNOWN_ERROR_STRING + statusCode, Toast.LENGTH_SHORT).show();
                                button.setVisibility(View.VISIBLE);
                                progressBarKirim.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                            button.setVisibility(View.VISIBLE);
                            progressBarKirim.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        callGetInfo.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                JSONObject body = null;
                int statusCode = response.code();
                Log.w("status", "status: " + statusCode);

                if (statusCode == 200) {
                    try {
                        body = new JSONObject(new Gson().toJson(response.body()));
                        Log.w("RESPONSE", "body: " + body.toString());

                        JSONArray clients = body.getJSONArray("data");
                        clientNames = new String[clients.length() + 1];
                        clientModels = new NasabahModel[clients.length() + 1];

                        clientNames[0] = DEFAULT_CLIENT_NAME;
                        for (int i = 0; i < clients.length(); i++) {
                            JSONObject client = clients.getJSONObject(i);
                            int id = client.getInt("client_id");
                            String name = client.getString("name");

                            String AMOUNT_KEY = "remaining_amount";
                            if (serviceId == AppConstant.DWK_SERVICE_ID_INTEGER) {
                                AMOUNT_KEY = "accepted_amount";
                            }
                            int remainingAmount = client.getInt(AMOUNT_KEY);

                            NasabahModel model = new NasabahModel(id, name, "", -1);
                            model.setRemainingClaimAmount(remainingAmount);

                            clientNames[i + 1] = name;
                            clientModels[i + 1] = model;
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                ClaimActivity.this, R.layout.item_spinner, clientNames) {
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
                                clientName = parent.getItemAtPosition(position).toString();
                                if (position != 0) {
                                    selectedModel = clientModels[position];
                                    int remainingAmount = selectedModel.getRemainingClaimAmount();

                                    if (remainingAmount < 25000000) {
                                        tvRemainingAmount.setTextColor(getResources().getColor(R.color.rejected));
                                    } else if (remainingAmount < 50000000) {
                                        tvRemainingAmount.setTextColor(getResources().getColor(R.color.waiting));
                                    } else {
                                        tvRemainingAmount.setTextColor(getResources().getColor(R.color.accepted));
                                    }

                                    if (serviceId == AppConstant.DWK_SERVICE_ID_INTEGER) {
                                        if (remainingAmount > 0) { // in DWK, remaining amount stores accepted amount
                                            tvRemainingAmountText.setTextColor(getResources().getColor(R.color.rejected));
                                            tvRemainingAmountText.setText("Nasabah sudah pernah melakukan claim");
                                            tvRemainingAmount.setText("");
                                            claimDwkAvailable = false;
                                        } else {
                                            tvRemainingAmountText.setTextColor(defaultTextColor);
                                            tvRemainingAmountText.setText("Klaim tersedia");
                                            claimDwkAvailable = true;
                                        }
                                    } else {
                                        tvRemainingAmountText.setText("Dana tersisa");
                                        tvRemainingAmount.setText(AppHelper.formatRupiah(remainingAmount));
                                    }
                                    selectedClientId = selectedModel.getId();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        progressBar.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (statusCode == AppConstant.HTTP_RESPONSE_401_UNAUTHORIZED) {
                    Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                    AppHelper.performLogout(response, sharedPrefManager, ClaimActivity.this, apiInterface);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), AppConstant.API_CALL_UNKNOWN_ERROR_STRING + statusCode, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public boolean validateInput() {
        String photoName = etPhoto.getText().toString();
        boolean isValid = true;

        if (clientName.equals(DEFAULT_CLIENT_NAME)) {
            ((TextView) spinner.getSelectedView()).setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            ((TextView) spinner.getSelectedView()).setError(null);
        }

        if (photoName.equals(DEFAULT_PHOTO_NAME)) {
            etPhoto.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etPhoto.setError(null);
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
            final String base64 = ImageUtil.convert(bitmap);

            if (requestCode == AppConstant.UPLOAD_FOTO_KLAIM) {
                AppHelper.showImageWithGlide(this, bitmap, imPhoto);
                etPhoto.setText(getResources().getString(R.string.klaim_ubahfoto));
                etPhoto.setError(null);
                photoBase64 = base64;

                imPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent fullScreenIntent = new Intent(ClaimActivity.this,
                                FullScreenImageActivity.class);
                        fullScreenIntent.putExtra("TYPE", SharedPrefManager.CLAIM_PHOTO_BASE64);
                        sharedPrefManager.saveString(SharedPrefManager.CLAIM_PHOTO_BASE64, photoBase64);
                        startActivity(fullScreenIntent);
                    }
                });
            }
        }
    }
}
