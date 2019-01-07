package com.jabezmagomere.karakanaics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class UserProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText etFirstName, etLastName, etUserName, etPhoneNumber, etEmail;
    private String FirstName, LastName, UserName, PhoneNumber, Email;
    private TextView tvProfileName, tvProfileEmail;
    private static final String UPDATE_URL="https://karakana.herokuapp.com/api/UpdateUser";
    private static final String GET_URL="https://karakana.herokuapp.com/api/Details";
    private RequestQueue requestQueue;
    private SessionManager sessionManager;
    private String token;
    public static final String FIRST_NAME="first_name";
    public static final String LAST_NAME="last_name";
    public static final String USER_NAME="user_name";
    public static final String PHONE_NUMBER="phone_number";
    public static final String EMAIL="email";
    public static final String TOKEN="token";
    private ProgressDialog progressDialog;
    HashMap<String,String> user=new HashMap<>();
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        toolbar=(Toolbar)findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");
        toolbar.setTitleTextColor(getResources().getColor(R.color.app_bkg));
        toolbar.setLogo(R.drawable.ic_person_user);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        requestQueue= Volley.newRequestQueue(UserProfile.this);
        sessionManager=new SessionManager(UserProfile.this);
        sessionManager.checkLogin();
        user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        tvProfileName=(TextView)findViewById(R.id.profile_name);
        tvProfileEmail=(TextView)findViewById(R.id.profile_email);
        etFirstName=(TextInputEditText) findViewById(R.id.etProfileFirstName);
        etLastName=(TextInputEditText) findViewById(R.id.etProfileLastName);
        etUserName=(TextInputEditText) findViewById(R.id.etProfileUserName);
        etPhoneNumber=(TextInputEditText) findViewById(R.id.etProfilePhoneNumber);
        etEmail=(TextInputEditText) findViewById(R.id.etProfileEmail);
        btnSave=(Button)findViewById(R.id.btnSave);
        loadDetails();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDetails();
            }
        });

    }



    public void updateUserDetails() {
        if (validateName(etFirstName) && validateName(etLastName) && validateName(etUserName) && validatePhone(etPhoneNumber) && validateEmail(etEmail)) {
            FirstName=etFirstName.getText().toString();
            LastName=etLastName.getText().toString();
            UserName=etUserName.getText().toString();
            PhoneNumber=etPhoneNumber.getText().toString();
            Email=etEmail.getText().toString();
            HashMap<String, String> params = new HashMap<>();
            params.put("FirstName", FirstName);
            params.put("LastName", LastName);
            params.put("UserName", UserName);
            params.put("PhoneNumber", PhoneNumber);
            params.put("email", Email);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    btnSave.setEnabled(true);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toasty.success(UserProfile.this, "Profile Updated", Toast.LENGTH_SHORT, true).show();
                    loadDetails();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    btnSave.setEnabled(true);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toasty.error(UserProfile.this, "An Error Occurred", Toast.LENGTH_SHORT, true).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("FirstName", FirstName);
                    params.put("LastName", LastName);
                    params.put("UserName", UserName);
                    params.put("email", Email);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer" + " " + token);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
            progressDialog = new ProgressDialog(UserProfile.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            btnSave.setEnabled(false);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);

        }

    }
    public void loadDetails(){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, GET_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                btnSave.setEnabled(true);
                try {
                    JSONObject response=jsonObject.getJSONObject("response");
                    Log.d("RESPONSE",response.toString());
                    tvProfileName.setText(response.getString("FirstName")+" "+response.getString("LastName"));
                    tvProfileEmail.setText(response.getString("email"));
                    etFirstName.setText(response.getString("FirstName"));
                    etLastName.setText(response.getString("LastName"));
                    etUserName.setText(response.getString("UserName"));
                    etPhoneNumber.setText(response.getString("PhoneNumber"));
                    etEmail.setText(response.getString("email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnSave.setEnabled(true);
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toasty.error(UserProfile.this,"An Error Occurred", Toast.LENGTH_SHORT,true).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Bearer"+" "+token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
        progressDialog=new ProgressDialog(UserProfile.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        btnSave.setEnabled(false);
        progressDialog.setMessage("Fetching...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

    }


    public boolean validateName(TextInputEditText textInputEditText){
        boolean status=true;
        if(TextUtils.isEmpty(textInputEditText.getText().toString())){
            Toasty.error(UserProfile.this,"All fields Required",Toast.LENGTH_SHORT).show();
            status=false;
        }
        return status;
    }
    public boolean validateEmail(TextInputEditText textInputEditText){
        boolean status=true;
        if(!TextUtils.isEmpty(textInputEditText.getText())&& Patterns.EMAIL_ADDRESS.matcher(textInputEditText.getText().toString()).matches()){
        }else{
            status=false;
            Toasty.error(UserProfile.this,"Invalid E-mail",Toast.LENGTH_SHORT).show();
        }
        return status;
    }
    public boolean validatePhone(TextInputEditText textInputEditText){
        boolean status=true;
        if(!TextUtils.isEmpty(textInputEditText.getText())&& textInputEditText.getText().toString().length()!=10){
            status=false;
            Toasty.error(UserProfile.this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
        }
        return status;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.swipeRight(UserProfile.this);
    }
}
