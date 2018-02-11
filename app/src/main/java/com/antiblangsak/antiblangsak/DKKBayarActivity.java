package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class DKKBayarActivity extends AppCompatActivity {

    private Spinner spPaymentMethod;
    private Spinner spBankName;

    private String bankName;
    private String paymentMethod;

    private final String DEFAULT_PAYMENT_METHOD = "Pilih Metode Pembayaran...";
    private final String DEFAULT_BANK_NAME = "Pilih Nama Bank...";

    ArrayList<NasabahBayarModel> nasabahBayarModels;
    public static NasabahBayarAdapter nasabahBayarModelsAdapter;
    private ListView listView;
    private static TextView totalTagihan;
    private static int tagihan;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private final String[] BANK_NAMES = new String[]{
            DEFAULT_BANK_NAME,
            "BNI",
            "BRI",
            "Bank Mandiri",
            "BCA",
            "Bank Permata",
            "Bank Bukopin"
    };

    private final String[] PAYMENT_METHOD = new String[]{
            DEFAULT_PAYMENT_METHOD,
            "Transfer Manual (ATM)",
            "Dua",
            "Tiga"
    };

    public static void addTagihan() {
        tagihan += 25000;
    }

    public static void reduceTagihan() {
        tagihan -= 25000;
    }

    public static void showTagihan() {
        totalTagihan.setText("Rp. " + tagihan + ",00");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dkkbayar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        final int id = sharedPrefManager.getId();
        final String token = sharedPrefManager.getToken();
        final String emailUser = sharedPrefManager.getEmail();

        Call call = apiInterface.prepayment(token, id);
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

                        int id = body.getJSONObject("data").getInt("id");
                        JSONArray familyMembersName = body.getJSONObject("data").getJSONArray("family_members");
                        JSONArray bankAccount = body.getJSONObject("data").getJSONArray("bank_accounts");

                        nasabahBayarModels = new ArrayList<>();

                        for(int i = 0; i < familyMembersName.length(); i++){
                            JSONObject member = (JSONObject) familyMembersName.get(i);
                            nasabahBayarModels.add(new NasabahBayarModel(member.getString("fullname")));
                        }

                        nasabahBayarModelsAdapter = new NasabahBayarAdapter(nasabahBayarModels, getApplicationContext());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.saveBoolean(SharedPrefManager.STATUS_LOGIN, false);
                        startActivity(new Intent(DKKBayarActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        DKKBayarActivity.this.finish();

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
            }
        });


        spBankName = findViewById(R.id.bankNameSpinner);
        ArrayAdapter<String> spinnerArrayAdapterBankName = new ArrayAdapter<String>(
                this, R.layout.item_spinner, BANK_NAMES) {
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

        spBankName.setAdapter(spinnerArrayAdapterBankName);
        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPaymentMethod = findViewById(R.id.paymentMethodSpinner);
        ArrayAdapter<String> spinnerArrayAdapterPaymentMethod = new ArrayAdapter<String>(
                this, R.layout.item_spinner, PAYMENT_METHOD) {
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

        spPaymentMethod.setAdapter(spinnerArrayAdapterPaymentMethod);
        spPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nasabahBayarModels = new ArrayList<>();
        nasabahBayarModels.add(new NasabahBayarModel("Kemas Ramadhan"));
        nasabahBayarModels.add(new NasabahBayarModel("Siti Muslihat"));
        nasabahBayarModels.add(new NasabahBayarModel("Hardi Ramli"));

        nasabahBayarModelsAdapter = new NasabahBayarAdapter(nasabahBayarModels, getApplicationContext());

        listView = findViewById(R.id.listNasabah);
        listView.setAdapter(nasabahBayarModelsAdapter);

        totalTagihan = findViewById(R.id.totalTagihan);
        totalTagihan.setText("Rp. " + tagihan + ",00");
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
