package com.jabezmagomere.karakanaics;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.jabezmagomere.karakanaics.Adapter.ProductAdapter;
import com.jabezmagomere.karakanaics.Model.Product;
import com.twigafoods.daraja.Daraja;
import com.twigafoods.daraja.DarajaListener;
import com.twigafoods.daraja.model.AccessToken;
import com.twigafoods.daraja.model.LNMExpress;
import com.twigafoods.daraja.model.LNMResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {
    private RecyclerView shopRecyclerView;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;
    private String token;
    private ProductAdapter productAdapter;
    private List<Product> productList=new ArrayList<>();
    private static final String TOKEN ="token";
    private static final String PRODUCT_URL="https://karakana.herokuapp.com/api/AllProducts";
    private static final String PURCHASE_URL="https://karakana.herokuapp.com/api/AddPurchase";
    private ProgressDialog progressDialog;
    private View view;
    private Daraja daraja;
    public final static String CONSUMER_KEY="ngjF4EGDnm1l3oXbZMcxy5eG8a3Palp8";
    public final static String CONSUMER_SECRET="VAj20GDH1XjPOeOk";
    private SweetAlertDialog sweetAlertDialog;
    String PhoneNumber;
    public static final String PHONE_NUMBER="phone_number";



    public ShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_shop, container, false);
        initCollapsingToolbar();
        shopRecyclerView=(RecyclerView)view.findViewById(R.id.shop_recycler_view);
        daraja = Daraja.with(CONSUMER_KEY,CONSUMER_SECRET, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i("TOKEN", accessToken.getAccess_token());
                Snackbar.make(view,accessToken.getAccess_token(),Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", error);
            }
        });
        SessionManager sessionManager=new SessionManager(getContext());
        HashMap<String,String> user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        PhoneNumber=user.get(PHONE_NUMBER);
        sessionManager.checkLogin();
        requestQueue= Volley.newRequestQueue(getContext());
        productAdapter=new ProductAdapter(productList, new ProductAdapter.ProductAdapterListener() {
            @Override
            public void imageButtonClick(View view, int position) {
                final Product product=productList.get(position);
                Toasty.info(getContext(),product.getProductName(),Toast.LENGTH_SHORT,true).show();
                                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setMessage("Are you sure you want to purchase "+product.getProductName()+" ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        progressDialog=new ProgressDialog(getContext());
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setMessage("Initializing Purchase...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setIndeterminate(true);
                        final LNMExpress lnmExpress = new LNMExpress(
                                "174379",
                                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                                product.getPrice(),
                                "254708374149",
                                "174379",
                                PhoneNumber,
                                "http://mycallbackurl.com/checkout.php",
                                "001ABC",
                                "Goods Payment"
                        );

                        daraja.requestMPESAExpress(lnmExpress,
                                new DarajaListener<LNMResult>() {
                                    @Override
                                    public void onResult(@NonNull LNMResult lnmResult) {
                                        Log.i("SHOW", lnmResult.ResponseDescription);
                                        Toasty.success(getContext(),lnmResult.ResponseDescription,Toast.LENGTH_SHORT,true).show();
                                        addPurchase(product.getProductId());
                                        if(progressDialog!=null){
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Log.i("ERROR", error);
                                        if(progressDialog!=null){
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        shopRecyclerView.setLayoutManager(layoutManager);
        shopRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(10),true));
        shopRecyclerView.setItemAnimator(new DefaultItemAnimator());
        shopRecyclerView.setAdapter(productAdapter);
        getProducts();
        return view;
    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Online Shop");
        AppBarLayout appBarLayout = (AppBarLayout)view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void getProducts(){
        productList.clear();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, PRODUCT_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject response=jsonArray.getJSONObject(i);
                        Product product=new Product(response.getString("id"),response.getString("ProductName"),response.getString("Category"),response.getString("Manufacturer"),response.getString("SerialNumber"),response.getString("Price"),response.getString("PhotoURL"),response.getString("ProductDescription"));
                        productList.add(product);
                        productAdapter.notifyDataSetChanged();
                    }

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
                Toasty.error(getContext(),"An error occured", Toast.LENGTH_SHORT,true).show();
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }

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
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }
    public void addPurchase(final String product_id){
        HashMap<String,String> params=new HashMap<>();
        params.put("product_id",product_id);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, PURCHASE_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sweetAlertDialog.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(),"An Error Occurred",Toast.LENGTH_SHORT,true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("product_id",product_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","Bearer"+" "+token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
        sweetAlertDialog=new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Purchase Status");
        sweetAlertDialog.setContentText("You have Successfully Purchased a Product");
        sweetAlertDialog.setConfirmText("OK");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                Intent intent=new Intent(getContext(),PurchaseActivity.class);
                startActivity(intent);
                Bungee.swipeLeft(getContext());
            }
        });
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
