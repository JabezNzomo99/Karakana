package com.jabezmagomere.karakanaics.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.jabezmagomere.karakanaics.Model.Product;
import com.jabezmagomere.karakanaics.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductCategory, tvProductManufacturer, tvProductSerialNumber, tvProductPrice, tvProductDescription;
        private ImageView ivProduct;
        private ImageButton imageButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.productName);
            tvProductCategory = itemView.findViewById(R.id.productCategory);
            tvProductDescription = itemView.findViewById(R.id.productDescription);
            tvProductManufacturer = itemView.findViewById(R.id.productManufacturer);
            tvProductSerialNumber = itemView.findViewById(R.id.SerialNumber);
            tvProductPrice = itemView.findViewById(R.id.productPrice);
            ivProduct = (ImageView) itemView.findViewById(R.id.thumbnail);
            imageButton = (ImageButton) itemView.findViewById(R.id.btnBuy);
        }
    }
    private List<Product> productList;
    public ProductAdapterListener listener;

    public ProductAdapter(List<Product> productList, ProductAdapterListener productAdapterListener) {
        this.productList = productList;
        this.listener=productAdapterListener;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_view,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.MyViewHolder holder, final int position) {
        final Product product=productList.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductManufacturer.setText(product.getManfacturer());
        holder.tvProductCategory.setText(product.getCategory());
        holder.tvProductDescription.setText(product.getDescription());
        holder.tvProductSerialNumber.setText(product.getSerialNumber());
        holder.tvProductPrice.setText(product.getPrice()+"/=");
        Picasso.get().load(product.getPhotoURL()).into(holder.ivProduct);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.imageButtonClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public interface ProductAdapterListener{
        void imageButtonClick(View view,int position);
    }


}
