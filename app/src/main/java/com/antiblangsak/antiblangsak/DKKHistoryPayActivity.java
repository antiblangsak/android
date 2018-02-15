package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DKKHistoryPayActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String token;
    private String emailUser;

    private ProgressBar progressBar;
    private ProgressBar progressBarButton;
    private LinearLayout main;

    private TextView nomorPembayaran;
    private TextView nominal;
    private TextView nasabah;
    private TextView metode;
    private TextView status;

    private TextView nomorPembayaranLabel;
    private TextView nominalLabel;
    private TextView nasabahLabel;
    private TextView metodeLabel;
    private TextView statusLabel;

    private Button btnBayar;
    private TextView informasi;

    private int histoID;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dkkhistory_pay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        Intent i = getIntent();
        // getting attached intent data
        histoID = i.getIntExtra("histoId", -1);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        progressBar = findViewById(R.id.progressBar);
        progressBarButton = findViewById(R.id.progressBarButton);
        main = findViewById(R.id.mainLayout);

        btnBayar = findViewById(R.id.btnBayar);
        informasi = findViewById(R.id.peringatan);

        nomorPembayaran = findViewById(R.id.nomorPembayaran);
        nominal = findViewById(R.id.nominal);
        nasabah = findViewById(R.id.nasabah);
        metode = findViewById(R.id.metode);
        status = findViewById(R.id.status);

        nomorPembayaranLabel = findViewById(R.id.nomorLabel);
        nominalLabel = findViewById(R.id.nominalLabel);
        nasabahLabel = findViewById(R.id.nasabahLabel);
        metodeLabel = findViewById(R.id.metodeLabel);
        statusLabel = findViewById(R.id.statusLabel);

        Typeface customFont = Typeface.createFromAsset(DKKHistoryPayActivity.this.getApplicationContext().getAssets(), "fonts/Comfortaa-Bold.ttf");
        nomorPembayaranLabel.setTypeface(customFont, Typeface.BOLD);
        nominalLabel.setTypeface(customFont, Typeface.BOLD);
        nasabahLabel.setTypeface(customFont, Typeface.BOLD);
        metodeLabel.setTypeface(customFont, Typeface.BOLD);
        statusLabel.setTypeface(customFont, Typeface.BOLD);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();

        Call call = apiInterface.getPaymentDetail(token, histoID);
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

                        String paymentNumber = data.getString("payment_number");;
                        int amount = Integer.parseInt(data.getString("payment_amount"));
                        JSONArray clients = data.getJSONArray("clients");

                        String client = "";
                        for (int i = 0; i < clients.length(); i++){
                            if (i == clients.length() - 1) {
                                client = client + clients.get(i);
                            } else {
                                client = client + clients.get(i) + "\n";
                            }
                        }

                        JSONObject bankAcc = data.getJSONObject("bank_account");
                        int stat = Integer.parseInt(data.getString("status"));
                        String bank = bankAcc.getString("bank_name");

                        nomorPembayaran.setText(paymentNumber);
                        nominal.setText(AppHelper.formatRupiah(amount));
                        nasabah.setText(client);
                        metode.setText(metode.getText()+ bank + "\n" + "1506004004000" + "\n" + "a/n PT. AntiBlangsak"
                        + "\n" + "Cabang Kedoya Permai");

                        if (stat == AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOR_PAYMENT){
                            status.setText(AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOR_PAYMENT_STRING);
                            status.setTextColor(getResources().getColor(R.color.waiting));

                            btnBayar.setVisibility(View.VISIBLE);

                            informasi.setVisibility(View.VISIBLE);
                            informasi.setText(AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOT_PAYMENT_NOTE);

                        } else if (stat == AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION){
                            status.setText(AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION_STRING);
                            status.setTextColor(getResources().getColor(R.color.waiting));

                            informasi.setVisibility(View.VISIBLE);
                            informasi.setText(AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION_NOTE);

                        } else if (stat == AppConstant.HISTORY_PAYMENT_STATUS_ACCEPTED){
                            status.setText(AppConstant.HISTORY_PAYMENT_STATUS_ACCEPTED_STRING);
                            status.setTextColor(getResources().getColor(R.color.accepted));
                        } else {
                            status.setText(AppConstant.HISTORY_PAYMENT_STATUS_REJECTED_STRING);
                            status.setTextColor(getResources().getColor(R.color.rejected));
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
                        startActivity(new Intent(DKKHistoryPayActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        DKKHistoryPayActivity.this.finish();

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
            }
        });

        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarButton.setVisibility(View.VISIBLE);
                btnBayar.setVisibility(View.GONE);
                informasi.setVisibility(View.GONE);

                Call call = apiInterface.confirmPayment(token, histoID, AppConstant.HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        JSONObject body = null;
                        int statusCode = response.code();
                        Log.w("status", "status: " + statusCode);

                        if (statusCode == 201) {
                            startActivity(new Intent(DKKHistoryPayActivity.this, DKKHistoryActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            DKKHistoryPayActivity.this.finish();
                            Toast.makeText(getApplicationContext(), "Konfirmasi berhasil!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Log.w("body", response.errorBody().string());
                                sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
                                startActivity(new Intent(DKKHistoryPayActivity.this, LoginActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                DKKHistoryPayActivity.this.finish();

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
                        progressBarButton.setVisibility(View.GONE);
                        btnBayar.setVisibility(View.VISIBLE);
                        informasi.setVisibility(View.VISIBLE);
                    }
                });
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
