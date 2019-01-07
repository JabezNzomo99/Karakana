package com.jabezmagomere.karakanaics;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jabezmagomere.karakanaics.Adapter.MechanicAdapter;
import com.jabezmagomere.karakanaics.Adapter.WindowAdapter;
import com.jabezmagomere.karakanaics.Model.Mechanic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class MechanicFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    private GoogleMap map;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private RequestQueue requestQueue;
    private static final String SOS_URL ="https://karakana.herokuapp.com/api/AllMechanics";
    private String token;
    private static final String TOKEN ="token";
    SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private MechanicAdapter mechanicAdapter;
    private List<Mechanic> mechanicList=new ArrayList<>();
    private Button btnRequestMechanic;
    private Location myLocation;
    private List<MyMechanic> myMechanicList=new ArrayList<>();
    String fName;
    String phoneNumber;


    public MechanicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 10, locationListener);
            }


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mechanic, container, false);
        mapView = (MapView) view.findViewById(R.id.mechanicMap);
        mapView.onCreate(savedInstanceState);
        btnRequestMechanic=(Button)view.findViewById(R.id.btnRequestMechanic);
        btnRequestMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                distanceFormulae(myLocation,myMechanicList);
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
        recyclerView=(RecyclerView)view.findViewById(R.id.mechancicRecyclerView);
        mechanicAdapter=new MechanicAdapter(mechanicList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(mechanicAdapter);
        requestQueue= Volley.newRequestQueue(getContext());
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();
        HashMap<String,String>user=sessionManager.getUserDetails();
        token=user.get(TOKEN);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            googleMap.setMyLocationEnabled(true);
        }
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style2));
            if (!success) {
                Log.e("ERROR", "Style parsing failed");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ERROR", e.getMessage());

        }
        locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                map.clear();
                myLocation=location;
                LatLng userLocation=new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your position").snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.manonmap)));
                CameraPosition cameraPosition=new CameraPosition.Builder().target(userLocation).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, SOS_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray=response.getJSONArray("response");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                LatLng RecoveryLocation=new LatLng(jsonObject.getDouble("Latitude"),jsonObject.getDouble("Longitude"));
                                Mechanic mechanic=new Mechanic(jsonObject.getString("FirstName"),jsonObject.getString("LastName"),jsonObject.getString("Email"),jsonObject.getString("PhoneNumber"),jsonObject.getString("Rating"),jsonObject.getString("Location"),jsonObject.getString("OpenTill"),jsonObject.getString("PhotoURL"),jsonObject.getString("Speciality"));
                                mechanicList.add(mechanic);
                                mechanicAdapter.notifyDataSetChanged();
                                MyMechanic myMechanic=new MyMechanic(jsonObject.getString("FirstName"),jsonObject.getString("PhoneNumber"),RecoveryLocation);
                                myMechanicList.add(myMechanic);
                                MarkerOptions markerOptions=new MarkerOptions().position(RecoveryLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mechaniconmap));
                                Marker marker=googleMap.addMarker(markerOptions);
                                marker.setTag(mechanic);
                                googleMap.setInfoWindowAdapter(new WindowAdapter(getContext()));



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
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }

                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String,String> headers=new HashMap<String, String>();
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

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permssion
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,10,locationListener);
            map.clear();
            Location lastknownlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastknownlocation!=null) {
                LatLng location = new LatLng(lastknownlocation.getLatitude(), lastknownlocation.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(location).title("Your position").snippet("Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.manonmap)));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(8).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }else{
                Toasty.info(getContext(),"Loading Location", Toast.LENGTH_SHORT, true).show();

            }

        }
    }
    public void distanceFormulae(Location location, List<MyMechanic> locationList){
        Boolean status=false;
        Double lat1=location.getLatitude();
        Double long1=location.getLongitude();
        for(int i=0;i<locationList.size();i++){
            MyMechanic mech=locationList.get(i);
            LatLng location1=mech.getLocation();
            Location location2=new Location(String.valueOf(location1));
            Log.d("LOCATIONS",location2.toString());
            Double lat2=location2.getLatitude();
            Double long2=location2.getLongitude();
            double distance=Math.hypot(lat2-lat1,long2-lat1);
            if(distance<=5){
                status=true;
               fName=mech.getMechanicName();
               phoneNumber=mech.getPhoneNumber();
            }else{
                status=false;
            }

        }
        if(status){
            SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitle("The nearest mechanic is");
            sweetAlertDialog.setContentText(fName);
            sweetAlertDialog.setConfirmText("CALL");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    ProgressDialog progressDialog=new ProgressDialog(getContext());
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phoneNumber));
                    getContext().startActivity(Intent.createChooser(intent,"Make call using..."));
                    if(progressDialog!=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
            });
            sweetAlertDialog.setCancelable(true);
            sweetAlertDialog.show();
        }else{
            SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setContentText("No Mechanic Located");
            sweetAlertDialog.show();
        }


    }
    private class MyMechanic{
        private String MechanicName, PhoneNumber;
        private LatLng location;

        public MyMechanic(String mechanicName, String phoneNumber, LatLng location) {
            MechanicName = mechanicName;
            PhoneNumber = phoneNumber;
            this.location = location;
        }

        public String getMechanicName() {
            return MechanicName;
        }

        public void setMechanicName(String mechanicName) {
            MechanicName = mechanicName;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            PhoneNumber = phoneNumber;
        }

        public LatLng getLocation() {
            return location;
        }

        public void setLocation(LatLng location) {
            this.location = location;
        }
    }

}
