package com.jabezmagomere.karakanaics.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jabezmagomere.karakanaics.Model.Purchase;
import com.jabezmagomere.karakanaics.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.MyViewHolder> {
    public class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView tvPurchaseName, tvPurchaseAmount, tvPurchaseStatus, tvPurchaseDate;
        private CircleImageView circleImageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPurchaseName=(TextView)itemView.findViewById(R.id.PurchaseName);
            tvPurchaseAmount=(TextView)itemView.findViewById(R.id.PurchaseAmount);
            tvPurchaseAmount=(TextView)itemView.findViewById(R.id.PurchaseAmount);
            tvPurchaseStatus=(TextView)itemView.findViewById(R.id.PurchaseStatus);
            tvPurchaseDate=(TextView)itemView.findViewById(R.id.PurchaseDate);
            circleImageView=(CircleImageView)itemView.findViewById(R.id.ivPurchase);
        }
    }
    private List<Purchase> purchaseList;

    public PurchaseAdapter(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_view,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Purchase purchase=purchaseList.get(position);
        holder.tvPurchaseName.setText(purchase.getProductName());
        holder.tvPurchaseAmount.setText("Ksh."+purchase.getAmount()+"/=");
        holder.tvPurchaseStatus.setText(purchase.getStatus());
        holder.tvPurchaseDate.setText(purchase.getPurchaseDate());
        Picasso.get().load(R.drawable.notepad).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }


}
