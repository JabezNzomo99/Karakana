package com.jabezmagomere.karakanaics;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jabezmagomere.karakanaics.Adapter.PurchaseAdapter;
import com.jabezmagomere.karakanaics.Model.Purchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spencerstudios.com.bungeelib.Bungee;

public class PurchaseActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    PurchaseAdapter purchaseAdapter;
    private List<Purchase> purchaseList=new ArrayList<>();
    SessionManager sessionManager;
    String token;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private static final String TOKEN ="token";
    private static final String PURCHASE_URL="https://karakana.herokuapp.com/api/ShowPurchase";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        requestQueue= Volley.newRequestQueue(this);
        recyclerView=(RecyclerView)findViewById(R.id.purchaseRecyclerView);
        purchaseAdapter=new PurchaseAdapter(purchaseList);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        sessionManager=new SessionManager(PurchaseActivity.this);
        sessionManager.checkLogin();
        HashMap<String,String> user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        toolbar=(Toolbar)findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Purchases");
        toolbar.setTitleTextColor(getResources().getColor(R.color.app_bkg));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.app_bkg,R.color.colorAccent,R.color.appbar_color);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PurchaseActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(PurchaseActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(purchaseAdapter);
                getPurchases();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPurchases();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.swipeRight(PurchaseActivity.this);
    }
    public void getPurchases(){
        swipeRefreshLayout.setRefreshing(true);
        purchaseList.clear();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, PURCHASE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("response");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Purchase purchase=new Purchase(jsonObject.getString("ProductName"),jsonObject.getString("Amount"),jsonObject.getString("Status"),jsonObject.getString("created_at"));
                        purchaseList.add(purchase);
                        purchaseAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
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

    }
}
