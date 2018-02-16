package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.adapters.HistoryAdapter;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.models.HistoryModel;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HistoryActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private ListView listView;
    private ArrayList<HistoryModel> history;
    private HistoryAdapter adapter;

    private ProgressBar progressBar;
    private TextView tvNoData;

    private String token;
    private int familyId;
    private String emailUser;
    private int serviceId;

    private Call call;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        familyId = sharedPrefManager.getFamilyId();

        serviceId = getIntent().getIntExtra(AppConstant.KEY_SERVICE_ID, -1);

        if (serviceId == AppConstant.DPGK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dpgk_color)));
            call = apiInterface.getDPGKHistory(token, familyId);
        } else if (serviceId == AppConstant.DKK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));
            call = apiInterface.getDKKHistory(token, familyId);
        } else if (serviceId == AppConstant.DWK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));
        } else {
            Toast.makeText(getApplicationContext(), "Invalid service", Toast.LENGTH_SHORT).show();
            finish();
        }

        listView = findViewById(R.id.mainlist);
        progressBar = findViewById(R.id.progressBar);
        tvNoData = findViewById(R.id.tvNoData);

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

                        history = new ArrayList<HistoryModel>();
                        JSONArray data = body.getJSONArray("data");
                        if (data.length() == 0) {
                            tvNoData.setVisibility(View.VISIBLE);
                        } else {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                String type = obj.getString("type");
                                int id = obj.getInt("id");
                                int status = Integer.parseInt(obj.getString("status"));
                                String createdAt = obj.getString("created_at");
                                Log.w("DATA", type + " " + id + " " + status + " " + createdAt);
                                history.add(new HistoryModel(id, type, status, createdAt));
                            }
                            adapter = new HistoryAdapter(history, HistoryActivity.this);
                            listView.setAdapter(adapter);
                            listView.setVisibility(View.VISIBLE);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                                    Log.w("error", "masuk sini");

                                    HistoryModel histo = adapter.getItem(position);
                                    Intent inten;
                                    if (histo.getType().equals("Pembayaran")) {
                                        inten = new Intent(HistoryActivity.this, HistoryPaymentActivity.class);
                                        inten.putExtra("histoId", histo.getId());
                                        inten.putExtra(AppConstant.KEY_SERVICE_ID, serviceId);
                                    } else {
                                        inten = new Intent(HistoryActivity.this, HistoryClaimActivity.class);
                                        inten.putExtra("claimId", histo.getId());
                                        inten.putExtra(AppConstant.KEY_SERVICE_ID, serviceId);
                                    }
                                    startActivity(inten);

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.logout();
                        startActivity(new Intent(HistoryActivity.this, LoginActivity.class)
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
