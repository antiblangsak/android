package com.antiblangsak.antiblangsak.common;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConfig;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.models.BankAccountModel;
import com.antiblangsak.antiblangsak.retrofit.ApiClient;
import com.antiblangsak.antiblangsak.retrofit.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvMemberSince;
    private TextView tvEmail;

    private ListView bankAccList;
    private TextView tvEndRekening;
    private ArrayAdapter<String> arrayAdapter;

    private ProgressBar progressBar;
    private LinearLayout upperLayout;
    private RelativeLayout lowerLayout;

    private LinearLayout logoutLayout;
    private TextView tvLogout;
    private ProgressBar progressBarLogout;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private int id;
    private String token;
    private String email;

    private ArrayList<BankAccountModel> bankAccountOBJ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMemberSince =  findViewById(R.id.tvMemberSince);
        bankAccList = findViewById(R.id.listRekening);
        tvEndRekening = findViewById(R.id.tvEndRekening);

        progressBar = findViewById(R.id.progressBar);
        upperLayout = findViewById(R.id.relativeLayout);
        lowerLayout = findViewById(R.id.relativeLayout2);

        logoutLayout = findViewById(R.id.logout);
        tvLogout = findViewById(R.id.tvLogout);
        progressBarLogout = findViewById(R.id.progressBarLogout);

        bankAccountOBJ = new ArrayList<BankAccountModel>();

        tvEndRekening.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, DaftarRekeningActivity.class)
                .putExtra("FROM", "Profile"));
                finish();
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tvLogout.setVisibility(View.GONE);
                progressBarLogout.setVisibility(View.VISIBLE);

                Call call = apiInterface.logout(token, email);
                call.enqueue(new Callback() {

                    @Override
                    public void onResponse(Call call, Response response) {
                        sharedPrefManager.logout();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        sharedPrefManager.logout();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                });
            }
        });

        id = sharedPrefManager.getId();
        token = sharedPrefManager.getToken();
        email = sharedPrefManager.getEmail();

        Log.w("GET PROFIlE", "ID " + id + " called");
        Log.w("GET PROFILE", "token " + token + " called");

        Call call = apiInterface.profile(token, id);
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
                        String name = body.getJSONObject("data").getString("name");
                        String memberSince = body.getJSONObject("data").getString("member_since");
                        String email = body.getJSONObject("data").getString("email");
                        String phone = body.getJSONObject("data").getString("phone_number");

                        JSONArray bankAccounts = body.getJSONObject("data").getJSONArray("bank_accounts");
                        String[] bankAccountsStr = new String[bankAccounts.length()];

                        for (int i = 0; i < bankAccounts.length(); i++) {
                            JSONObject bankAccount = bankAccounts.getJSONObject(i);

                            String nam = bankAccount.getString("account_name");
                            String bn = bankAccount.getString("bank_name");
                            int idd = bankAccount.getInt("id");
                            String cab = bankAccount.getString("branch_name");
                            String an = bankAccount.getString("account_number");

                            bankAccountsStr[i] = nam + " - " + bn;

                            bankAccountOBJ.add(new BankAccountModel(idd, bn, nam, an, cab));

                        }

                        tvName.setText(name);
                        tvMemberSince.setText(memberSince);
                        tvEmail.setText(email);

                        arrayAdapter = new ArrayAdapter<String>(ProfileActivity.this, R.layout.listitem_rekening, bankAccountsStr);
                        bankAccList.setAdapter(arrayAdapter);

                        bankAccList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                // custom dialog
                                final Dialog dialog = new Dialog(ProfileActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                                WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.
                                params.y = -100; // Here is the param to set your dialog position. Same with params.x
                                dialog.getWindow().setAttributes(params);

                                dialog.setContentView(R.layout.view_rekening_nasabah);
                                //dialog.setTitle("Info Rekening");

                                BankAccountModel bankacc = bankAccountOBJ.get(i);
                                Typeface boldFont = Typeface.createFromAsset(ProfileActivity.this.getAssets(), AppConfig.BOLD_FONT);

                                // set the custom dialog components - text, image and button
                                TextView info = dialog.findViewById(R.id.infoRekening);
                                TextView namaBank = (TextView) dialog.findViewById(R.id.namaBank);
                                TextView cabang = (TextView) dialog.findViewById(R.id.cabang);
                                TextView noRek = (TextView) dialog.findViewById(R.id.noRek);
                                TextView atasNama = (TextView) dialog.findViewById(R.id.atasNama);

                                info.setTypeface(boldFont);
                                namaBank.setText(bankacc.getBankName());
                                cabang.setText(bankacc.getBranchName());
                                noRek.setText(bankacc.getAccountNumber());
                                atasNama.setText(bankacc.getAccountName());

                                TextView dialogButton = dialog.findViewById(R.id.done);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                            }
                        });

                        lowerLayout.setVisibility(View.VISIBLE);
                        upperLayout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.logout();
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        ProfileActivity.this.finish();

                        Call callLogout = apiInterface.logout(token, email);
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
                finish();
            }
        });

    }

    private void setStackedBackgroundDrawable() {
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
