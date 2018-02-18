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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.adapters.NasabahAdapter;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.AppHelper;
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

public class NasabahAddActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String token;
    private String emailUser;
    private int userId;

    private ListView nasabahListView;
    private ArrayList<NasabahModel> nasabahModels;
    private static NasabahAdapter nasabahAdapter;

    private Button btnDaftarkan;
    private ProgressBar progressBar;

    private int serviceId;
    private ColorDrawable checkedColor;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasabah_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        userId = sharedPrefManager.getId();

        serviceId = getIntent().getIntExtra(AppConstant.KEY_SERVICE_ID, -1);

        if (serviceId == AppConstant.DPGK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dpgk_color)));
            checkedColor = new ColorDrawable(getResources().getColor(R.color.dpgk_nasabah_checked_color));
        } else if (serviceId == AppConstant.DKK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));
            checkedColor = new ColorDrawable(getResources().getColor(R.color.dkk_nasabah_checked_color));
        } else if (serviceId == AppConstant.DWK_SERVICE_ID_INTEGER) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dwk_color)));
            checkedColor = new ColorDrawable(getResources().getColor(R.color.dwk_nasabah_checked_color));
        } else {
            Toast.makeText(getApplicationContext(), "Invalid service", Toast.LENGTH_SHORT).show();
            finish();
        }

        nasabahListView = findViewById(R.id.list_view_nasabah);
        nasabahModels = new ArrayList<NasabahModel>();

        btnDaftarkan = findViewById(R.id.btnDaftarkan);
        progressBar = findViewById(R.id.pbDaftarkan);

        JSONArray unregisteredMembers = null;
        try {
            unregisteredMembers = new JSONArray(getIntent().getStringExtra(AppConstant.KEY_UNREGISTERED_MEMBERS_JSON));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
            finish();
        }

        try {
            for (int i = 0; i < unregisteredMembers.length(); i++) {
                JSONObject obj = unregisteredMembers.getJSONObject(i);
                int id = obj.getInt("id");
                String name = obj.getString("fullname");
                String relation = obj.getString("relation");
                Log.w("DATA", id + " | " + name + " | " + relation);
                nasabahModels.add(new NasabahModel(id, name, relation, 0));
            }
            nasabahAdapter = new NasabahAdapter(nasabahModels, NasabahAddActivity.this, false);
            nasabahListView.setAdapter(nasabahAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
            finish();
        }

        nasabahListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NasabahModel nasabahModel = nasabahAdapter.getItem(position);
                nasabahModel.pressed();
                ImageView checked = view.findViewById(R.id.checked);

                if (nasabahModel.isSelected()) {
                    checked.setVisibility(View.VISIBLE);
                    view.setBackground(checkedColor);
                } else {
                    checked.setVisibility(View.GONE);
                    view.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
                }
            }
        });

        btnDaftarkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> ids = new ArrayList<>();
                for (NasabahModel nasabahModel : nasabahModels) {
                    if (nasabahModel.isSelected()) {
                        Log.w("PRESSED", "id: " + nasabahModel.getId());
                        ids.add(nasabahModel.getId());
                    }
                }

                if (ids.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Pilih minimal satu anggota keluarga Anda", Toast.LENGTH_SHORT).show();
                } else {
                    btnDaftarkan.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    Call call = apiInterface.registerClient(token, userId, ids, serviceId);
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

                                    startActivity(new Intent(NasabahAddActivity.this, NasabahActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .putExtra(AppConstant.KEY_SERVICE_ID, serviceId));
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                                }
                            } else if (statusCode == AppConstant.HTTP_RESPONSE_401_UNAUTHORIZED) {
                                Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                                AppHelper.performLogout(response, sharedPrefManager, NasabahAddActivity.this, apiInterface);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), AppConstant.API_CALL_UNKNOWN_ERROR_STRING + statusCode, Toast.LENGTH_SHORT).show();
                                btnDaftarkan.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_LONG).show();
                            btnDaftarkan.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
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
