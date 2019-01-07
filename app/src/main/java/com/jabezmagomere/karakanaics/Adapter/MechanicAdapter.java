package com.jabezmagomere.karakanaics.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jabezmagomere.karakanaics.Model.Mechanic;
import com.jabezmagomere.karakanaics.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MechanicAdapter extends RecyclerView.Adapter<MechanicAdapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMechanicName, tvMechanicRating, tvMecahnicLocation, tvSpeciality;
        private CircleImageView circleImageView;
        private Button btnCall, btnText, btnMail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMechanicName=itemView.findViewById(R.id.tvMechanicName);
            tvMechanicRating=itemView.findViewById(R.id.tvMechanicRating);
            tvMecahnicLocation=itemView.findViewById(R.id.tvMechanicArea);
            tvSpeciality=itemView.findViewById(R.id.tvSpeciality);
            circleImageView=itemView.findViewById(R.id.ivMechanic);
            btnCall=(Button)itemView.findViewById(R.id.btnCall);
            btnText=(Button)itemView.findViewById(R.id.btnText);
            btnMail=(Button)itemView.findViewById(R.id.btnMail);
        }
    }
    private List<Mechanic> mechanicList;

    public MechanicAdapter(List<Mechanic> mechanicList) {
        this.mechanicList = mechanicList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mechanic_view,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Mechanic mechanic=mechanicList.get(position);
        holder.tvMechanicName.setText(mechanic.getFirstName()+" "+mechanic.getLastName());
        holder.tvMechanicRating.setText(mechanic.getRating());
        holder.tvMecahnicLocation.setText(mechanic.getLocation());
        holder.tvSpeciality.setText(mechanic.getSpeciallity());
        Picasso.get().load(mechanic.getPhotoURL()).into(holder.circleImageView);
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mechanic.getPhoneNumber()));
                view.getContext().startActivity(Intent.createChooser(intent,"Make call using..."));
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        });
        holder.btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:"+mechanic.getPhoneNumber()));
                intent.putExtra("sms_body","Hi "+mechanic.getFirstName()+"!");
                view.getContext().startActivity(Intent.createChooser(intent,"Send text using..."));
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });
        holder.btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {mechanic.getEmail()});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MECHANIC BOOKING");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Dear "+mechanic.getFirstName());
                view.getContext().startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mechanicList.size();
    }


}
