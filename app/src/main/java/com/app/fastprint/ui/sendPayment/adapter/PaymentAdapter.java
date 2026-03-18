package com.app.fastprint.ui.sendPayment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.app.fastprint.R;
import com.app.fastprint.ui.sendPayment.sendPaymentResponseModel.SendPaymentResponseModel;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    Context context;
    onPaypalClickListener onPaypalClickListener;
    List<SendPaymentResponseModel.Data.SendPayment> sendPayment;

    public PaymentAdapter(Context context, List<SendPaymentResponseModel.Data.SendPayment> sendPayment,
                          onPaypalClickListener onPaypalClickListener) {
        this.context = context;
        this.sendPayment = sendPayment;
        this.onPaypalClickListener = onPaypalClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvPaymentTpe.setText(sendPayment.get(position).getName());
        if (sendPayment.get(position).getImage() != null) {
            Glide.with(context).load(sendPayment.get(position).getImage())
                    .placeholder(R.drawable.ic_refresh)
                    .into(holder.imgPayment);
        } else {
            Glide.with(context).load(sendPayment.get(position).getImage())
                    .placeholder(R.drawable.ic_refresh)
                    .into(holder.imgPayment);
        }

        holder.itemView.setOnClickListener(v -> onPaypalClickListener.onPaypalClick());
    }

    @Override
    public int getItemCount() {
        return sendPayment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPayment;
        TextView tvPaymentTpe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPayment = itemView.findViewById(R.id.imgPayment);
            tvPaymentTpe = itemView.findViewById(R.id.tvPaymentTpe);
            tvPaymentTpe.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        }
    }

    public interface onPaypalClickListener {
        void onPaypalClick();
    }
}
