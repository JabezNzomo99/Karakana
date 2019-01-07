package com.jabezmagomere.karakanaics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jabezmagomere.karakanaics.Model.Mechanic;
import com.jabezmagomere.karakanaics.Model.Recovery;
import com.jabezmagomere.karakanaics.R;


import es.dmoral.toasty.Toasty;

public class WindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View mWindow;
    private Context context;
    public WindowAdapter(Context context){
        this.context=context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.custom_window,null);

    }
    public void renderWindow(Marker marker, View view){
        TextView tvMechanicName, tvMechLocation, tvMechEmail, tvTime;
        Mechanic mechanic=(Mechanic) marker.getTag();
        if(marker.getTag()==null){
            Toasty.info(view.getContext(),"User Location", Toast.LENGTH_SHORT,true).show();

        }else{
            tvMechanicName=(TextView)view.findViewById(R.id.tvMechName);
            if(!mechanic.getFirstName().equals("")){
                tvMechanicName.setText(mechanic.getFirstName()+" "+mechanic.getLastName());
            }
            tvMechLocation=(TextView)view.findViewById(R.id.tvMechLocation);
            if(!mechanic.getLocation().equals("")){
                tvMechLocation.setText(mechanic.getLocation());

            }
            tvMechEmail=(TextView)view.findViewById(R.id.tvMechEmail);
            if(!mechanic.getEmail().equals("")){
                tvMechEmail.setText(mechanic.getEmail());
            }
            tvTime=(TextView)view.findViewById(R.id.tvTime);
            if(!mechanic.getOpenTill().equals("")){
                tvTime.setText("Open Till: "+mechanic.getOpenTill());
            }

        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {

        renderWindow(marker,mWindow);
        return mWindow;
    }
}
