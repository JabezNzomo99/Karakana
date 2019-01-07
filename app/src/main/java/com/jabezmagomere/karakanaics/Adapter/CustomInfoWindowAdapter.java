package com.jabezmagomere.karakanaics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jabezmagomere.karakanaics.Model.Recovery;
import com.jabezmagomere.karakanaics.R;


import es.dmoral.toasty.Toasty;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private View mWindow;
    private Context context;
    public CustomInfoWindowAdapter(Context context){
        this.context=context;
        mWindow= LayoutInflater.from(context).inflate(R.layout.custom_info_window,null);

    }
    public void renderWindow(Marker marker, View view){
        TextView tvRecoveryName, tvLocation, tvEmail;
            Recovery recovery=(Recovery) marker.getTag();
        if(marker.getTag()==null){
            Toasty.info(view.getContext(),"User Location", Toast.LENGTH_SHORT,true).show();

        }else{
            tvRecoveryName=(TextView)view.findViewById(R.id.tvRecoveryName);
            if(!recovery.getRecoveryName().equals("")){
                tvRecoveryName.setText(recovery.getRecoveryName());
            }
            tvLocation=(TextView)view.findViewById(R.id.tvLocation);
            if(!recovery.getLocation().equals("")){
                tvLocation.setText(recovery.getLocation());

            }
            tvEmail=(TextView)view.findViewById(R.id.tvRecoveryEmail);
            if(!recovery.getEmail().equals("")){
                tvEmail.setText(recovery.getEmail());
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
