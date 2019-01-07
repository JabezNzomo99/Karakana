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

import com.jabezmagomere.karakanaics.Model.Recovery;
import com.jabezmagomere.karakanaics.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecoveryAdapter extends RecyclerView.Adapter<RecoveryAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRecoveryName, tvRating,tvArea;
        private CircleImageView circleImageView;
        private Button btnCallRecovery;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecoveryName=itemView.findViewById(R.id.tvRecovery);
            tvRating=itemView.findViewById(R.id.tvRating);
            tvArea=itemView.findViewById(R.id.tvArea);
            circleImageView=itemView.findViewById(R.id.ivRecovery);
            btnCallRecovery=itemView.findViewById(R.id.btnCallRecovery);
        }
    }
    private List<Recovery> recoveryList;

    public RecoveryAdapter(List<Recovery> recoveryList) {
        this.recoveryList = recoveryList;
    }

    @NonNull
    @Override
    public RecoveryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recovery_view,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecoveryAdapter.MyViewHolder holder, int position) {
        final Recovery recovery=recoveryList.get(position);
        holder.tvRecoveryName.setText(recovery.getRecoveryName());
        holder.tvRating.setText(recovery.getRating());
        holder.tvArea.setText(recovery.getLocation());
        Picasso.get().load(recovery.getPhotoURL()).into(holder.circleImageView);
        holder.btnCallRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+recovery.getPhoneNumber()));
                view.getContext().startActivity(Intent.createChooser(intent,"Make call using..."));
                if(progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return recoveryList.size();
    }
}
