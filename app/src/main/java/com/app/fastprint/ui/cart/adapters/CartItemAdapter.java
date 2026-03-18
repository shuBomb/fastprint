package com.app.fastprint.ui.cart.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.fastprint.R;
import com.app.fastprint.ui.cart.responseModel.CartListing;
import com.app.fastprint.ui.productDetails.ProductDetailsActivity;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    Context context;
    OnDeleteCartListener onDeleteCartListener;
    ArrayList<CartListing> cartListing;
    String selectedCurrency="",JOD="",USD="";
    Double currencyRateApplied=0.0;

    public CartItemAdapter(Context context, ArrayList<CartListing> cartListings, OnDeleteCartListener onDeleteCartListener,String selectedCurrency,Double currencyRateApplied,String JOD,String USD) {
        this.context = context;
        this.cartListing = cartListings;
        this.onDeleteCartListener = onDeleteCartListener;
        this.selectedCurrency=selectedCurrency;
        this.currencyRateApplied=currencyRateApplied;
        this.JOD=JOD;
        this.USD=USD;
    }

    public void customNotify(ArrayList<CartListing> cartListings) {
        this.cartListing = cartListings;
        notifyDataSetChanged();
    }
    public void updateparsm(String selectedCurrency,Double currencyRateApplied,String JOD,String USD) {
        this.selectedCurrency=selectedCurrency;
        this.currencyRateApplied=currencyRateApplied;
        this.JOD=JOD;
        this.USD=USD;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_product_categories, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvDiscountPrice.setPaintFlags(holder.tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvDiscountPrice.setText(cartListing.get(position).getCurrencySymbol() + cartListing.get(position).getDiscount_amount());

        String valuePrice = cartListing.get(position).getAmount();
        if(selectedCurrency == "JOD"){
            Double valueP=Double.parseDouble(valuePrice);
            valueP=valueP/currencyRateApplied;
            String strV= String.format("%.2f", valueP);
            holder.tvPrice.setText(JOD + " " + strV);

        }
        else{
            holder.tvPrice.setText(USD + " " + valuePrice);

        }

      //  holder.tvPrice.setText(cartListing.get(position).getCurrencySymbol() + cartListing.get(position).getAmount());
        holder.tvProductName.setText(cartListing.get(position).getProduct_name());
        holder.tvQuantity.setText(cartListing.get(position).getQuantity());
        holder.tvtotalRating.setText(cartListing.get(position).getTotal_rating());
        Glide.with(context).load(cartListing.get(position).getProduct_image()).into(holder.imgProduct);

        holder.rating.setRating(cartListing.get(position).getRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id", cartListing.get(position).getProduct_id());
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(v -> onDeleteCartListener.onDeleteProduct(position));
    }

    @Override
    public int getItemCount() {
        return cartListing.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tvProductName;
        private ImageView imgDelete;
        private TextView tvDiscountPrice;
        private TextView tvPrice;
        private TextView tvQuantity;
        private TextView tvtotalRating;
        private ImageView imgProduct;
        private RatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvProductName = itemView.findViewById(R.id.tvProductName);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            tvDiscountPrice = itemView.findViewById(R.id.tvDiscountPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvtotalRating = itemView.findViewById(R.id.tvtotalRating);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            rating = itemView.findViewById(R.id.rating);



            tvProductName.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
            tvDiscountPrice.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
            tvPrice.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        }
    }

    public interface OnDeleteCartListener {
        void onDeleteProduct(int position);
    }
}
