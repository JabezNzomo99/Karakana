package com.jabezmagomere.karakanaics;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private String FirstName, LastName, UserName, PhoneNumber, Email, Password;
    private TextInputEditText etFirstName,etUserName, etLastName,etPhoneNumber, etEmail,etPassword,etConfirmPassword;
    private TextInputLayout layout_firstName, layout_lastName,layout_PhoneNumber,layout_UserName,layout_Email, layout_Password, layoutConfirmPassword;
    public static final String URL="https://karakana.herokuapp.com/api/Register";
    RequestQueue requestQueue;
    Button btnRegister;
    private ProgressDialog progressDialog;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register, container, false);
        etFirstName=(TextInputEditText) view.findViewById(R.id.etFirstName);
        etLastName=(TextInputEditText) view.findViewById(R.id.etLastName);
        etUserName=(TextInputEditText) view.findViewById(R.id.etUserName);
        etPhoneNumber=(TextInputEditText)view.findViewById(R.id.etPhoneNumber);
        etEmail=(TextInputEditText) view.findViewById(R.id.etEmail);
        etPassword=(TextInputEditText) view.findViewById(R.id.etPass);
        etConfirmPassword=(TextInputEditText) view.findViewById(R.id.etConfirmPassword);
        layout_firstName=(TextInputLayout)view.findViewById(R.id.layout_firstName);
        layout_lastName=(TextInputLayout)view.findViewById(R.id.layout_lastName);
        layout_PhoneNumber=(TextInputLayout)view.findViewById(R.id.layout_phoneNumber);
        layout_UserName=(TextInputLayout)view.findViewById(R.id.layout_userName);
        layout_Email=(TextInputLayout)view.findViewById(R.id.layout_Email);
        layout_Password=(TextInputLayout)view.findViewById(R.id.layout_Password);
        layoutConfirmPassword=(TextInputLayout)view.findViewById(R.id.layout_confirmPassword);
        requestQueue= Volley.newRequestQueue(getContext());
        btnRegister=(Button)view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateName(etFirstName,layout_firstName)&&validateName(etLastName,layout_lastName) &&validatePhone(etPhoneNumber,layout_PhoneNumber)&&
                        validateName(etUserName,layout_UserName)&&validateEmail(etEmail,layout_Email)&&validatePassword(etPassword,layout_Password)&&validatePassword(etConfirmPassword,layoutConfirmPassword)&& checkMatchPassword()){
                    FirstName=etFirstName.getText().toString();
                    LastName=etLastName.getText().toString();
                    UserName=etUserName.getText().toString();
                    PhoneNumber=etPhoneNumber.getText().toString();
                    Email=etEmail.getText().toString();
                    Password=etPassword.getText().toString();
                    Register(); }

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
    }
    private boolean validatePassword(TextInputEditText textInputEditText, TextInputLayout passwordLayout){
        boolean status=true;
        if(TextUtils.isEmpty(textInputEditText.getText().toString()) || textInputEditText.getText().toString().length()<6 ){
            passwordLayout.setError("Invalid Password");
            status=false;
        }
        return status;
    }
    public boolean validateEmail(TextInputEditText textInputEditText,TextInputLayout textInputLayout){
        boolean status=true;
        if(!TextUtils.isEmpty(textInputEditText.getText())&& Patterns.EMAIL_ADDRESS.matcher(textInputEditText.getText().toString()).matches()){
            textInputLayout.setErrorEnabled(false);
        }else{
            status=false;
            textInputLayout.setError("Invalid E-mail");
        }
        return status;
    }
    public boolean checkMatchPassword(){
        boolean status=true;
        if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            status = false;
            layoutConfirmPassword.setError("Password must match");
        }
        return status;

    }
    public boolean validatePhone(TextInputEditText textInputEditText,TextInputLayout textInputLayout){
        boolean status=true;
        if(!TextUtils.isEmpty(textInputEditText.getText())&& textInputEditText.getText().toString().length()!=10){
            status=false;
            textInputLayout.setError("Invalid Phone Number");
        }
        return status;
    }
    public void Register(){
        HashMap<String,String> params=new HashMap<>();
        params.put("FirstName",FirstName);
        params.put("LastName",LastName);
        params.put("UserName",UserName);
            params.put("PhoneNumber",PhoneNumber);
        params.put("email",Email);
        params.put("password",Password);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                btnRegister.setEnabled(true);
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toasty.success(getContext(),"Account Created", Toast.LENGTH_SHORT,true).show();
                Intent intent=new Intent(getContext(),LoginRegHost.class);
                startActivity(intent);
                Bungee.swipeRight(getContext());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    btnRegister.setEnabled(true);
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                Toasty.error(getContext(),error.getMessage(), Toast.LENGTH_SHORT,true).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("FirstName",FirstName);
                params.put("LastName",LastName);
                params.put("UserName",UserName);
                params.put("email",Email);
                params.put("password",Password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Accept","application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        btnRegister.setEnabled(false);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();



    }

}

