package com.jabezmagomere.karakanaics;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private String UserName,Password;
    private TextInputEditText etUserName, etPassword;
    private TextInputLayout layoutUserName, layoutPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private static final String LOGIN_URL="https://karakana.herokuapp.com/api/Login";
    private static final String LOG_TAG=LoginFragment.class.getSimpleName();
    private SessionManager sessionManager;
    private View view;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_login, container, false);
        requestQueue= Volley.newRequestQueue(getContext());
        sessionManager=new SessionManager(getContext());
        layoutUserName=(TextInputLayout)view.findViewById(R.id.layoutUserName);
        layoutPassword=(TextInputLayout)view.findViewById(R.id.layoutPassword);
        etUserName=(TextInputEditText) view.findViewById(R.id.etUserName);
        etPassword=(TextInputEditText) view.findViewById(R.id.etPassword);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateName(etUserName,layoutUserName) && validatePassword(etPassword,layoutPassword)){
                    loginUser();
                }

            }
        });

        return view;
    }
    public boolean validateName(TextInputEditText textInputEditText, TextInputLayout textInputLayout){
        boolean status=true;
        if(TextUtils.isEmpty(textInputEditText.getText().toString())){
            textInputLayout.setError("Field Required");
            status=false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return status;
    }private boolean validatePassword(TextInputEditText textInputEditText, TextInputLayout passwordLayout){
        boolean status=true;
        if(TextUtils.isEmpty(textInputEditText.getText().toString()) || textInputEditText.getText().toString().length()<6 ){
            passwordLayout.setError("Invalid Password");
            status=false;
        }
        return status;
    }
    public void loginUser(){
        UserName=etUserName.getText().toString();
        Password=etPassword.getText().toString();
        HashMap<String,String> params=new HashMap<String, String>();
        params.put("UserName",UserName);
        params.put("password",Password);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, LOGIN_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                btnLogin.setEnabled(true);
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toasty.success(getContext(),"Login Success",Toast.LENGTH_SHORT,true).show();
                try {
                    JSONObject response=jsonObject.getJSONObject("response");
                    String token=response.getString("token");
                    Snackbar.make(view,token,Snackbar.LENGTH_LONG).show();
                    JSONObject user=response.getJSONObject("user");
                    sessionManager.setUser_Id(user.getString("id"));
                    sessionManager.setFirstName(user.getString("FirstName"));
                    sessionManager.setLastName(user.getString("LastName"));
                    sessionManager.setUserName(user.getString("UserName"));
                    sessionManager.setPhoneNumber(user.getString("PhoneNumber"));
                    sessionManager.setEmail(user.getString("email"));
                    sessionManager.setToken(token);
                    sessionManager.loginUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(sessionManager.isLoggedIn()){
                    Intent intent=new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                    Bungee.swipeLeft(getContext());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnLogin.setEnabled(true);
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                Log.d(LOG_TAG,error.toString());
                Toasty.error(getContext(),"Login Failed", Toast.LENGTH_SHORT, true).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<String, String>();
                params.put("UserName",UserName);
                params.put("password",Password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers=new HashMap<String, String>();
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        btnLogin.setEnabled(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

}
