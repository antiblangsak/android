package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DKKHistoryClaimActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String token;
    private int familyId;
    private String emailUser;

    private ProgressBar progressBar;
    private LinearLayout main;

    private TextView nomorKlaim;
    private TextView nominal;
    private TextView nasabah;
    private TextView detail;
    private TextView status;

    private TextView nomorPembayaranLabel;
    private TextView nominalLabel;
    private TextView nasabahLabel;
    private TextView detailLabel;
    private TextView statusLabel;

    private TextView informasi;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dkkhistory_claim);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        Intent i = getIntent();
        int claimId = i.getIntExtra("claimId", -1);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.mainLayout);

        nomorKlaim = findViewById(R.id.nomorKlaim);
        nominal = findViewById(R.id.nominal);
        nasabah = findViewById(R.id.nasabah);
        detail = findViewById(R.id.detail);
        status = findViewById(R.id.status);
        informasi = findViewById(R.id.peringatan);

        nomorPembayaranLabel = findViewById(R.id.nomorLabel);
        nominalLabel = findViewById(R.id.nominalLabel);
        nasabahLabel = findViewById(R.id.nasabahLabel);
        detailLabel = findViewById(R.id.detailLabel);
        statusLabel = findViewById(R.id.statusLabel);

        Typeface customFont = Typeface.createFromAsset(DKKHistoryClaimActivity.this.getApplicationContext().getAssets(), AppConfig.BOLD_FONT);
        nomorPembayaranLabel.setTypeface(customFont, Typeface.BOLD);
        nominalLabel.setTypeface(customFont, Typeface.BOLD);
        nasabahLabel.setTypeface(customFont, Typeface.BOLD);
        detailLabel.setTypeface(customFont, Typeface.BOLD);
        statusLabel.setTypeface(customFont, Typeface.BOLD);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        familyId = sharedPrefManager.getFamilyId();

        Call call = apiInterface.getClaimDetail(token, claimId);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                JSONObject body = null;
                int statusCode = response.code();
                Log.w("status", "status: " + statusCode);

                if (statusCode == 200) {
                    try {
                        body = new JSONObject(new Gson().toJson(response.body()));
                        Log.w("RESPONSE", "body: " + body.toString());

                        JSONObject data = body.getJSONObject("data");
                        String claimNumber = data.getString("claim_number");
                        String client = data.getString("client");
                        int claim_amount = data.getInt("claim_amount");
                        String note = data.getString("note");
                        int statusKlaim = data.getInt("status");

                        nomorKlaim.setText(claimNumber);
                        nasabah.setText(client);
                        nominal.setText(AppHelper.formatRupiah(claim_amount));
                        detail.setText(note);

                        if (statusKlaim == AppConstant.HISTORY_CLAIM_STATUS_WAITING_FOR_VERIFICATION) {
                            status.setText(AppConstant.HISTORY_CLAIM_STATUS_WAITING_FOR_VERIFICATION_STRING);
                            status.setTextColor(getResources().getColor(R.color.waiting));
                            informasi.setText(AppConstant.HISTORY_CLAIM_STATUS_WAITING_FOR_VERIFICATION_NOTE);
                        } else if (statusKlaim == AppConstant.HISTORY_CLAIM_STATUS_ACCEPTED) {
                            status.setText(AppConstant.HISTORY_CLAIM_STATUS_ACCEPTED_STRING);
                            status.setTextColor(getResources().getColor(R.color.accepted));
                            informasi.setText(AppConstant.HISTORY_CLAIM_STATUS_ACCEPTED_NOTE);
                        } else {
                            status.setText(AppConstant.HISTORY_CLAIM_STATUS_REJECTED_STRING);
                            status.setTextColor(getResources().getColor(R.color.rejected));
                            informasi.setText(AppConstant.HISTORY_CLAIM_STATUS_REJECTED_NOTE);
                        }
                        main.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
                        startActivity(new Intent(DKKHistoryClaimActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        DKKHistoryClaimActivity.this.finish();

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
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
                finish();
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
