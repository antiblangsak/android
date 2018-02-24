package com.antiblangsak.antiblangsak.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.antiblangsak.antiblangsak.R;
import com.antiblangsak.antiblangsak.adapters.FamilyMemberAdapter;
import com.antiblangsak.antiblangsak.app.AppConstant;
import com.antiblangsak.antiblangsak.app.AppHelper;
import com.antiblangsak.antiblangsak.app.SharedPrefManager;
import com.antiblangsak.antiblangsak.models.FamilyMemberModel;
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

public class FamilyProfileActivity extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;
    private ApiInterface apiInterface;

    private String token;
    private String emailUser;
    private int familyId;

    private TextView tvIdKeluarga;
    private TextView tvFamilyNumber;
    private TextView tvChairman;
    private TextView tvAddress;
    private TextView tvRtRw;
    private TextView tvPostalCode;
    private TextView tvVillage;
    private TextView tvSubDistrict;
    private TextView tvCity;
    private TextView tvProvince;

    private NonScrollListView listFamilyMember;
    private ArrayList<FamilyMemberModel> listMembers;
    private FamilyMemberAdapter familyMemberAdapter;

    private ScrollView mainLayout;
    private ProgressBar progressBar;

    private Animation slideUp;
    private Animation slideDown;

    private LinearLayout layoutFamilyDataHeader;
    private NestedScrollView layoutFamilyData;
    private LinearLayout layoutFamilyMemberHeader;
    private NestedScrollView layoutFamilyMember;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPrefManager = new SharedPrefManager(this);

        slideUp = AnimationUtils.loadAnimation(FamilyProfileActivity.this, R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(FamilyProfileActivity.this, R.anim.slide_down);

        token = sharedPrefManager.getToken();
        emailUser = sharedPrefManager.getEmail();
        familyId = sharedPrefManager.getFamilyId();

        tvIdKeluarga = findViewById(R.id.tvIdKeluarga);
        tvFamilyNumber = findViewById(R.id.tvFamilyNumber);
        tvChairman = findViewById(R.id.tvChairman);
        tvAddress = findViewById(R.id.tvAddress);
        tvRtRw = findViewById(R.id.tvRtRw);
        tvPostalCode = findViewById(R.id.tvPostalCode);
        tvVillage = findViewById(R.id.tvVillage);
        tvSubDistrict = findViewById(R.id.tvSubDistrict);
        tvCity = findViewById(R.id.tvCity);
        tvProvince = findViewById(R.id.tvProvince);

        listFamilyMember = findViewById(R.id.listFamilyMember);

        mainLayout = findViewById(R.id.mainLayout);
        progressBar = findViewById(R.id.progressBar);

        layoutFamilyDataHeader = findViewById(R.id.layoutFamilyDataHeader);
        layoutFamilyData = findViewById(R.id.layoutFamilyData);
        layoutFamilyMemberHeader = findViewById(R.id.layoutFamilyMemberHeader);
        layoutFamilyMember = findViewById(R.id.layoutFamilyMember);

        layoutFamilyData.setVisibility(View.GONE);
        layoutFamilyDataHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutFamilyData.getVisibility() == View.GONE) {
                    expand(layoutFamilyData, 550);
                } else {
                    collapse(layoutFamilyData);
                }
            }
        });

        layoutFamilyMember.setVisibility(View.GONE);
        layoutFamilyMemberHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutFamilyMember.getVisibility() == View.GONE) {
                    expand(layoutFamilyMember, 550);
                } else {
                    collapse(layoutFamilyMember);
                }
            }
        });

        Call call = apiInterface.getFamilyProfile(token, familyId);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                JSONObject body = null;
                int statusCode = response.code();
                Log.w("status", "status: " + statusCode);

                if (statusCode == 200) {
                    try {
                        body = new JSONObject(new Gson().toJson(response.body()));

                        JSONObject data = body.getJSONObject("data");

                        tvIdKeluarga.setText("ID KELUARGA: " + data.getInt("id"));
                        tvFamilyNumber.setText(data.getString(("kk_number")));
                        tvChairman.setText(data.getString("family_head_name"));
                        tvAddress.setText(data.getString("address"));
                        tvRtRw.setText(data.getString("rt_rw"));
                        tvPostalCode.setText(data.getString("postal_code"));
                        tvVillage.setText(data.getString("village"));
                        tvSubDistrict.setText(data.getString("subdistrict"));
                        tvCity.setText(data.getString("city"));
                        tvProvince.setText(data.getString("province"));

                        JSONArray familyMembers = data.getJSONArray("family_members");
                        listMembers = new ArrayList<>();
                        for (int i = 0; i < familyMembers.length(); i++) {
                            JSONObject familyMemberData = familyMembers.getJSONObject(i);
                            int id = familyMemberData.getInt("id");
                            String name = familyMemberData.getString("fullname");
                            String relation = familyMemberData.getString("relation");
                            listMembers.add(new FamilyMemberModel(id, name, relation, familyMemberData));
                            Log.w("FAMILY MEMBER", "json: " + familyMemberData.toString());
                        }
                        familyMemberAdapter = new FamilyMemberAdapter(listMembers, getApplicationContext());
                        listFamilyMember.setAdapter(familyMemberAdapter);

                        //martin add clickable
                        listFamilyMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                try {
                                Intent inten;
                                    inten = new Intent(FamilyProfileActivity.this, FamilyMemberActivity.class);
                                    JSONObject member = listMembers.get(position).getData();
                                    Log.w("lst",member+"");
                                    String fullname = member.getString("fullname");
                                    String nik = member.getString("nik");
                                    String gender = member.getString("gender");
                                    String birth_place = member.getString("birth_place");
                                    String birth_date = member.getString("birth_date");
                                    String religion = member.getString("religion");
                                    String education = member.getString("education");
                                    String occupation = member.getString("occupation");
                                    String marital_status = member.getString("marital_status");
                                    String relation = member.getString("relation");
                                    String nationality = member.getString("nationality");
                                    String passport_license = member.getString("passport_license");
                                    String father_name = member.getString("father_name");
                                    String mother_name = member.getString("mother_name");

                                    inten.putExtra("fullname", member.getString("fullname"));
                                    inten.putExtra("nik", member.getString("nik"));
                                    inten.putExtra("gender", member.getString("gender"));
                                    inten.putExtra("birth_place", member.getString("birth_place"));
                                    inten.putExtra("birth_date", member.getString("birth_date"));
                                    inten.putExtra("religion", member.getString("religion"));
                                    inten.putExtra("education", member.getString("education"));
                                    inten.putExtra("occupation", member.getString("occupation"));
                                    inten.putExtra("marital_status", member.getString("marital_status"));
                                    inten.putExtra("relation", member.getString("relation"));
                                    inten.putExtra("nationality", member.getString("nationality"));
                                    inten.putExtra("passport_license", member.getString("passport_license"));
                                    inten.putExtra("father_name", member.getString("father_name"));
                                    inten.putExtra("mother_name", member.getString("mother_name"));

                                    startActivity(inten);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });





                        mainLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error ketika parsing JSON!", Toast.LENGTH_SHORT).show();
                    }
                } else if (statusCode == AppConstant.HTTP_RESPONSE_401_UNAUTHORIZED) {
                    Toast.makeText(getApplicationContext(), AppConstant.SESSION_EXPIRED_STRING, Toast.LENGTH_SHORT).show();
                    AppHelper.performLogout(response, sharedPrefManager, FamilyProfileActivity.this, apiInterface);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), AppConstant.API_CALL_UNKNOWN_ERROR_STRING + statusCode, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
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

    private ValueAnimator slideAnimator(int start, int end, final View view) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void expand(View view, int heightToExpand) {
        //set Visible
        view.setVisibility(View.VISIBLE);

        ValueAnimator mAnimator = slideAnimator(0, heightToExpand, view);
        mAnimator.start();
    }

    private void collapse(final View view) {
        int finalHeight = view.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, view);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

}

class NonScrollListView extends ListView {

    public NonScrollListView(Context context) {
        super(context);
    }
    public NonScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NonScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
