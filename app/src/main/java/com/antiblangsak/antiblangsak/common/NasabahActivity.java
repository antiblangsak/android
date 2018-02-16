package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.adapters.NasabahAdapter;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.models.NasabahModel;
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

public class NasabahActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private ListView nasabahListView;
    private ArrayList<NasabahModel> nasabahModels;
    private static NasabahAdapter nasabahAdapter;

    private ProgressBar progressBar;
    private LinearLayout mainLayout;
    private LinearLayout tvBelumAdaNasabah;
    private LinearLayout addNewNasabah;

    private String token;
    private int familyId;
    private String emailUser;

    private JSONArray registered;
    private JSONArray unregistered;

    private int serviceId;
    private Call call;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        familyId = sharedPrefManager.getFamilyId();

        serviceId = getIntent().getIntExtra(AppConstant.KEY_SERVICE_ID, -1);

        if (serviceId == AppConstant.DPGK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dpgk_color)));
            call = apiInterface.getAllDPGKFamilyMembers(token, familyId);
        } else if (serviceId == AppConstant.DKK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));
            call = apiInterface.getAllDKKFamilyMembers(token, familyId);
        } else if (serviceId == AppConstant.DWK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));
        } else {
            Toast.makeText(getApplicationContext(), "Invalid service", Toast.LENGTH_SHORT).show();
            finish();
        }

        nasabahListView = findViewById(R.id.list_view_nasabah);
        addNewNasabah = findViewById(R.id.layout_add_nasabah);
        tvBelumAdaNasabah = findViewById(R.id.tvBelumAdaNasabah);

        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.mainLayout);

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
                        registered = data.getJSONArray("registered");
                        unregistered = data.getJSONArray("unregistered");

                        nasabahModels = new ArrayList<NasabahModel>();
                        for (int i = 0; i < registered.length(); i++) {
                            JSONObject obj = registered.getJSONObject(i);
                            int id = obj.getInt("id");
                            String name = obj.getString("fullname");
                            String relation = obj.getString("relation");
                            int status = Integer.parseInt(obj.getString("status"));
                            Log.w("DATA", id + " | " + name + " | " + relation + " | " + status);
                            nasabahModels.add(new NasabahModel(id, name, relation, status));
                        }
                        nasabahAdapter = new NasabahAdapter(nasabahModels, getApplicationContext(), true);
                        nasabahListView.setAdapter(nasabahAdapter);

                        mainLayout.setVisibility(View.VISIBLE);

                        if (registered != null && registered.length() > 0) {
                            tvBelumAdaNasabah.setVisibility(View.GONE);
                        }

                        if (unregistered == null || unregistered.length() == 0) {
                            addNewNasabah.setVisibility(View.GONE);
                        } else {
                            addNewNasabah.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent myIntent = new Intent(NasabahActivity.this, NasabahAddActivity.class)
                                            .putExtra(AppConstant.KEY_UNREGISTERED_MEMBERS_JSON, unregistered.toString())
                                            .putExtra(AppConstant.KEY_SERVICE_ID, serviceId);
                                    startActivity(myIntent);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.logout();
                        startActivity(new Intent(NasabahActivity.this, LoginActivity.class)
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
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
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
