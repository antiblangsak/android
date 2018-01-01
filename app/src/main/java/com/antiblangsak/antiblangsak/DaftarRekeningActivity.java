package com.antiblangsak.antiblangsak;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DaftarRekeningActivity extends AppCompatActivity {

    private Spinner spBankName;
    private EditText etBranchName;
    private EditText etAccountNumber;
    private EditText etAccountHolderName;
    private EditText etAccountPhoto;
    private Button btnDaftarkanRekening;
    private PhotoView imAccountPhoto;

    private String bankName;
    private String branchName;
    private String accountNumber;
    private String accountHolderName;
    private String accountPhoto;

    private final String DEFAULT_BANK_NAME = "Pilih Nama Bank...";
    private String DEFAULT_PHOTO_NAME;

    private final String[] BANK_NAMES = new String[]{
            DEFAULT_BANK_NAME,
            "BNI",
            "BRI",
            "Bank Mandiri",
            "BCA",
            "Bank Permata",
            "Bank Bukopin"
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_rekening);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.daftarnasabah_title);

        DEFAULT_PHOTO_NAME = getResources().getString(R.string.daftarrekening_accountphoto);

        etBranchName = findViewById(R.id.etBranchName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etAccountHolderName = findViewById(R.id.etAccountHolderName);
        etAccountPhoto = findViewById(R.id.etAccountPhoto);
        etAccountPhoto.setFocusable(false);
        etAccountPhoto.setClickable(true);
        imAccountPhoto = findViewById(R.id.imAccountPhoto);

        spBankName = findViewById(R.id.bankNameSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
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

        spBankName.setAdapter(spinnerArrayAdapter);
        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etAccountPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("etAccountPhoto", "CLICKED");
                Intent openGalleryIntent = new Intent();
                openGalleryIntent.setType("image/*");
                openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),
                        AppConstant.DAFTAR_REKENING_REQUEST_GALLERY);
            }
        });

        btnDaftarkanRekening = findViewById(R.id.btnDaftarkanRekening);
        btnDaftarkanRekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    Log.v("btnDaftarkanRekening", "VALID INPUT");
                    Intent myIntent = new Intent(DaftarRekeningActivity.this,
                            DaftarNasabahUploadFotoActivity.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getApplicationContext(), AppConstant.GENERAL_MISSING_FIELD_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                    Log.v("btnDaftarkanRekening", "INVALID INPUT");
                }
            }
        });

        findViewById(R.id.mainLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                etBranchName.clearFocus();
                etAccountNumber.clearFocus();
                etAccountHolderName.clearFocus();
                etAccountPhoto.clearFocus();
                return true;
            }
        });
    }

    public boolean validateInput() {
        branchName = etBranchName.getText().toString();
        accountNumber = etAccountNumber.getText().toString();
        accountHolderName = etAccountHolderName.getText().toString();
        accountPhoto = etAccountPhoto.getText().toString();

        boolean isValid = true;

        if(bankName.equals(DEFAULT_BANK_NAME)){
            ((TextView) spBankName.getSelectedView()).setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            ((TextView) spBankName.getSelectedView()).setError(null);
        }

        if(branchName.isEmpty()){
            etBranchName.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etBranchName.setError(null);
        }

        if(accountNumber.isEmpty()){
            etAccountNumber.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etAccountNumber.setError(null);
        }

        if(accountHolderName.isEmpty()){
            etAccountHolderName.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etAccountHolderName.setError(null);
        }

        if(accountPhoto.equals(DEFAULT_PHOTO_NAME)){
            etAccountPhoto.setError(AppConstant.GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE);
            isValid = false;
        } else {
            etAccountPhoto.setError(null);
        }

        return isValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.DAFTAR_REKENING_REQUEST_GALLERY && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                final Uri imageUri = data.getData();
                InputStream is = getContentResolver().openInputStream(imageUri);
                Log.v("INPUT STREAM", "Image size: " + is.available());

                AppHelper.showImageThumbnail(imageUri, this, imAccountPhoto, etAccountPhoto);
                etAccountPhoto.setText(AppHelper.getFileName(this, imageUri));
                etAccountPhoto.setError(null);

                byte[] imageBytes = AppHelper.getBytes(is);
                uploadImage(imageBytes);

                imAccountPhoto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent fullScreenIntent = new Intent(DaftarRekeningActivity.this,
                                FullScreenImageActivity.class);
                        fullScreenIntent.setData(imageUri);
                        startActivity(fullScreenIntent);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void uploadImage(byte[] imageBytes) {
        Log.v("IMAGE_BYTES", Arrays.toString(imageBytes));
    }
}
