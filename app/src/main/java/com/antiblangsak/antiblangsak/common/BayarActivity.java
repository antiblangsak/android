package com.antiblangsak.antiblangsak.common;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.adapters.NasabahBayarAdapter;
import com.antiblangsak.antiblangsak.models.NasabahBayarModel;
import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.dkk.DKKActivity;
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

public class BayarActivity extends AppCompatActivity {

    private Spinner spPaymentMethod;
    private Spinner spBankName;

    private String bankName;
    private String paymentMethod;

    private final String DEFAULT_PAYMENT_METHOD = "Pilih Metode Pembayaran...";
    private final String DEFAULT_BANK_NAME = "Pilih Nama Bank...";

    private ArrayList<NasabahBayarModel> nasabahBayarModels;
    public static NasabahBayarAdapter nasabahBayarModelsAdapter;
    private NonScrollListView listView;
    private static TextView totalTagihan;
    private static int tagihan;

    private TextView tvNomorRekening;
    private TextView tvPemilikRekening;
    private TextView tvNomorRekeningTitle;
    private TextView tvPemilikRekeningTitle;

    private ApiInterface apiInterface;
    private SharedPrefManager sharedPrefManager;

    private String[] bankAccountsName;
    private String[] bankAccountsNumber;
    private String[] bankAccountsOwner;
    private int[] bankAccountsId;
    private int bankAccountIndex;

    private String[] PAYMENT_METHOD = new String[]{
            "Transfer Manual (ATM)",
    };

    private int serviceId;

    private LinearLayout mainLayout;
    private ProgressBar progressBar;
    private ProgressBar progressBarBayar;

    private ArrayList<Integer> selectedClients;

    public static void addTagihan() {
        tagihan += 25000;
    }

    public static void reduceTagihan() {
        tagihan -= 25000;
    }

    public static void showTagihan() {
        totalTagihan.setText("Rp. " + tagihan + ",00");
    }

    private Button btnBayar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serviceId = getIntent().getIntExtra(AppConstant.KEY_SERVICE_ID, -1);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dkk_color)));

        final int familyId = sharedPrefManager.getFamilyId();
        final String token = sharedPrefManager.getToken();
        final String emailUser = sharedPrefManager.getEmail();
        final int userId = sharedPrefManager.getId();

        mainLayout = findViewById(R.id.mainLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBarBayar = findViewById(R.id.progressBarBayar);

        selectedClients = new ArrayList();

        Call call_prepayment = apiInterface.getDKKPaymentInfo(token, familyId);
        call_prepayment.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {

                JSONObject body = null;
                int statusCode = response.code();
                Log.w("status", "status: " + statusCode);

                if (statusCode == 200) {
                    try {
                        body = new JSONObject(new Gson().toJson(response.body()));
                        Log.w("RESPONSE", "body: " + body.toString());

                        JSONArray familyMembersName = body.getJSONObject("data").getJSONArray("family_members");
                        JSONArray bankAccount = body.getJSONObject("data").getJSONArray("bank_accounts");

                        nasabahBayarModels = new ArrayList<>();

                        for(int i = 0; i < familyMembersName.length(); i++){
                            JSONObject member = (JSONObject) familyMembersName.get(i);
                            nasabahBayarModels.add(new NasabahBayarModel(member.getInt("client_id"), member.getString("fullname")));
                        }

                        nasabahBayarModelsAdapter = new NasabahBayarAdapter(nasabahBayarModels, getApplicationContext());

                        listView = findViewById(R.id.listNasabah);
                        listView.setAdapter(nasabahBayarModelsAdapter);
                        listView.setVisibility(View.VISIBLE);

                        bankAccountsName = new String[bankAccount.length() + 1];
                        bankAccountsNumber = new String[bankAccount.length()+1];
                        bankAccountsOwner = new String[bankAccount.length()+1];
                        bankAccountsId = new int[bankAccount.length()+1];
                        bankAccountsName[0] = DEFAULT_BANK_NAME;

                        for(int i = 0; i < bankAccount.length(); i++){
                            bankAccountsName[i+1] = bankAccount.getJSONObject(i).getString("bank_name");
                            bankAccountsNumber[i+1] = bankAccount.getJSONObject(i).getString("account_number");
                            bankAccountsOwner[i+1] = bankAccount.getJSONObject(i).getString("account_name");
                            bankAccountsId[i+1] = bankAccount.getJSONObject(i).getInt("id");
                        }

                        ArrayAdapter<String> spinnerArrayAdapterBankName = new ArrayAdapter<String>(
                                BayarActivity.this, R.layout.item_spinner, bankAccountsName) {
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
                                tvNomorRekening = findViewById(R.id.nomorRekening);
                                tvPemilikRekening = findViewById(R.id.pemilikRekening);
                                tvNomorRekeningTitle = findViewById(R.id.nomorRekeningTitle);
                                tvPemilikRekeningTitle = findViewById(R.id.pemilikRekeningTitle);

                                if(position > 0){
                                    tvNomorRekening.setVisibility(View.VISIBLE);
                                    tvPemilikRekening.setVisibility(View.VISIBLE);
                                    tvNomorRekeningTitle.setVisibility(View.VISIBLE);
                                    tvPemilikRekeningTitle.setVisibility(View.VISIBLE);
                                    tvNomorRekening.setText(bankAccountsNumber[position]);
                                    tvPemilikRekening.setText(bankAccountsOwner[position]);
                                    bankAccountIndex = position;
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
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    try {
                        Log.w("body", response.errorBody().string());
                        sharedPrefManager.logout();
                        startActivity(new Intent(BayarActivity.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
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
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
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

        totalTagihan = findViewById(R.id.totalTagihan);
        totalTagihan.setText("Rp. " + tagihan + ",00");


        btnBayar = findViewById(R.id.btnBayar);
        btnBayar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validateInput()) {
                    return;
                }
                btnBayar.setVisibility(View.GONE);
                progressBarBayar.setVisibility(View.VISIBLE);

                int bank_account_id = bankAccountsId[bankAccountIndex];

                for(int i = 0; i < nasabahBayarModels.size(); i++){
                    if(nasabahBayarModels.get(i).isChecked()){
                        selectedClients.add(nasabahBayarModels.get(i).getId());
                    }
                }
                Log.w("selectedClients", selectedClients.toString());

                Call call_payment = apiInterface.payment(token, serviceId, bank_account_id, userId, selectedClients, tagihan);
                call_payment.enqueue(new Callback() {
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
                                int paymentId = data.getInt("id");

                                startActivity(new Intent(BayarActivity.this, HistoryPaymentActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra(AppConstant.KEY_SERVICE_ID, serviceId)
                                        .putExtra("histoId", paymentId));
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            try {
                                Log.w("body", response.errorBody().string());
                                sharedPrefManager.logout();
                                startActivity(new Intent(BayarActivity.this, LoginActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
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
                        btnBayar.setVisibility(View.GONE);
                        progressBarBayar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public boolean validateInput() {
        boolean isValid = true;

        ArrayList<Integer> selectedClients = new ArrayList<>();
        for(int i = 0; i < nasabahBayarModels.size(); i++){
            if(nasabahBayarModels.get(i).isChecked()){
                selectedClients.add(nasabahBayarModels.get(i).getId());
            }
        }

        if (selectedClients.size() == 0) {
            isValid = false;
            Toast.makeText(getApplicationContext(), AppConstant.BAYAR_NO_CLIENT_SELECTED_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        if (bankAccountIndex == 0) {
            ((TextView) spBankName.getSelectedView()).setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            if (isValid) {
                Toast.makeText(getApplicationContext(), AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
            isValid = false;
        } else {
            ((TextView) spBankName.getSelectedView()).setError(null);
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
}
