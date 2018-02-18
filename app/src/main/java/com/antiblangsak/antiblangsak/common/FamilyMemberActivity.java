package com.antiblangsak.antiblangsak.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.models.FamilyMemberModel;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FamilyMemberActivity extends AppCompatActivity {

    String fullname ;
    String nik ;
    String gender ;
    String birth_place ;
    String birth_date ;
    String religion ;
    String education ;
    String occupation ;
    String marital_status ;
    String relation ;
    String nationality ;
    String passport_license ;
    String father_name ;
    String mother_name ;

    private TextView tvFullName ;
    private TextView tvNik ;
    private TextView tvGender ;
    private TextView tvBirthPlace ;
    private TextView tvBirthDate ;
    private TextView tvReligion ;
    private TextView tvEducation ;
    private TextView tvOccupation ;
    private TextView tvMaritalStatus ;
    private TextView tvRelation ;
    private TextView tvNationality ;
    private TextView tvPassportLicense ;
    private TextView tvFatherName ;
    private TextView tvMotherName ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_member);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        fullname = getIntent().getStringExtra("fullname");
        nik = getIntent().getStringExtra("nik");
        gender = getIntent().getStringExtra("gender");
        birth_place = getIntent().getStringExtra("birth_place");
        birth_date = getIntent().getStringExtra("birth_date");
        religion = getIntent().getStringExtra("religion");
        education = getIntent().getStringExtra("education");
        occupation = getIntent().getStringExtra("occupation");
        marital_status = getIntent().getStringExtra("marital_status");
        relation = getIntent().getStringExtra("relation");
        nationality = getIntent().getStringExtra("nationality");
        passport_license = getIntent().getStringExtra("passport_license");
        father_name = getIntent().getStringExtra("father_name");
        mother_name = getIntent().getStringExtra("mother_name");

        tvFullName =  findViewById(R.id.tvFullName);
        tvNik =  findViewById(R.id.tvNik);
        tvGender =  findViewById(R.id.tvGender);
        tvBirthPlace =  findViewById(R.id.tvBirthPlace);
        tvBirthDate =  findViewById(R.id.tvBirthDate);
        tvReligion =  findViewById(R.id.tvReligion);
        tvEducation =  findViewById(R.id.tvEducation);
        tvOccupation =  findViewById(R.id.tvOccupation);
        tvMaritalStatus =  findViewById(R.id.tvMaritalStatus);
        tvRelation =  findViewById(R.id.tvRelation);
        tvNationality =  findViewById(R.id.tvNationality);
        tvPassportLicense =  findViewById(R.id.tvPassportLicense);
        tvFatherName =  findViewById(R.id.tvFatherName);
        tvMotherName =  findViewById(R.id.tvMotherName);

        tvFullName.setText(fullname);
        tvNik.setText(nik);
        tvGender.setText(gender);
        tvBirthPlace.setText(birth_place);
        tvBirthDate.setText(birth_date);
        tvReligion.setText(religion);
        tvEducation.setText(education);
        tvOccupation.setText(occupation);
        tvMaritalStatus.setText(marital_status);
        tvRelation.setText(relation);
        tvNationality.setText(nationality);
        tvPassportLicense.setText(passport_license);
        tvFatherName.setText(father_name);
        tvMotherName.setText(mother_name);
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
