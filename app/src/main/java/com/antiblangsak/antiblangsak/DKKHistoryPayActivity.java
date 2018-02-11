package com.antiblangsak.antiblangsak;

import android.content.Context;
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
    private int familyId;
    private String emailUser;

    private ProgressBar progressBar;
    private LinearLayout main;

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
        int histoID = i.getIntExtra("histoId", -1);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        progressBar = findViewById(R.id.progressBar);
        main = findViewById(R.id.mainLayout);

        final Button btnBayar = findViewById(R.id.btnBayar);
        final TextView informasi = findViewById(R.id.peringatan);

        final TextView nomorPembayaran = findViewById(R.id.nomorPembayaran);
        final TextView nominal = findViewById(R.id.nominal);
        final TextView nasabah = findViewById(R.id.nasabah);
        final TextView metode = findViewById(R.id.metode);
        final TextView status = findViewById(R.id.status);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        familyId = sharedPrefManager.getFamilyId();

        Call call = apiInterface.pay(token, histoID);
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

                        String paymentNumber = "";
                        int amount = 0;
                        JSONArray clients = null;
                        String client = "";
                        JSONObject bankAcc = null;
                        String bank = "";
                        String accNumber = "";
                        String refUser = "";
                        int stat = 0;

                        Log.w("RESPONSE", "Tai dah");

                        paymentNumber = data.getString("payment_number");;
                        amount = Integer.parseInt(data.getString("payment_amount"));
                        clients = data.getJSONArray("clients");
                        bankAcc = data.getJSONObject("bank_account");
                        stat = Integer.parseInt(data.getString("status"));
                        Log.w("DATA", paymentNumber + " " + amount + " " + clients + " " + bankAcc);



                        main.setVisibility(View.VISIBLE);

                        nomorPembayaran.setText(paymentNumber);
                        nominal.setText(amount+"");

                        for (int i = 0; i < clients.length(); i++){
                            client = client + clients.get(i) + "\n";
                        }
                        nasabah.setText(client);

                        bank = bankAcc.getString("bank_name");


                        metode.setText(metode.getText()+ bank + "\n" + "1506004004000" + "\n" + "a/n PT. AntiBlangsak"
                        + "\n" + "Cabang Kedoya Permai");

                        if (stat == 0){
                            status.setText("Menunggu Pembarayan");
                            status.setTextColor(getResources().getColor(R.color.waiting));

                            btnBayar.setVisibility(View.VISIBLE);
                            informasi.setVisibility(View.VISIBLE);

                        } else if (stat == 1){
                            status.setText("Menunggu Konfirmasi");
                            status.setTextColor(getResources().getColor(R.color.waiting));

                            informasi.setVisibility(View.VISIBLE);
                            informasi.setText("Sistem sedang melakukan verifikasi terhadap pembayaran anda. Mohon tunggu beberapa saat.");

                        } else if (stat == 2){
                            status.setText("Diterima");
                            status.setTextColor(getResources().getColor(R.color.accepted));
                        } else {
                            status.setText("Ditolak");
                            status.setTextColor(getResources().getColor(R.color.rejected));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
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
