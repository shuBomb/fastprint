package com.app.fastprint.ui.productDetails.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fastprint.R;
import com.app.fastprint.ui.productDetails.ProductDetailsActivity;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.app.fastprint.ui.productDetails.ProductDetailsActivity.tvColor;
import static com.app.fastprint.ui.productDetails.ProductDetailsActivity.tvPaperType;
import static com.app.fastprint.ui.productDetails.ProductDetailsActivity.tvSize;

public class ProductPaperTypeAdapter extends RecyclerView.Adapter<ProductPaperTypeAdapter.ViewHolder> {
    Context context;
    List<String> productPaperTypeList;
    private int mCheckedPostion = -1;// no selection by default
    ProductDetailsActivity productDetailsActivity;
    TextView tvPaperType;
    Dialog dialog;
    public ProductPaperTypeAdapter(Context context, TextView tvPaperType,
                                   List<String> productPaperTypeList, ProductDetailsActivity
                                          productDetailsActivity, Dialog dialog) {
        this.context = context;
        this.productPaperTypeList = productPaperTypeList;
        this.productDetailsActivity = productDetailsActivity;
        this.dialog = dialog;
        this.tvPaperType = tvPaperType;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_paper_type, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.chPaperType.setChecked(position == mCheckedPostion);
        holder.chPaperType.setText(productPaperTypeList.get(position));
        holder.chPaperType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == mCheckedPostion) {
                    holder.chPaperType.setChecked(false);
                    mCheckedPostion = -1;
                } else {
                    mCheckedPostion = position;
                    notifyDataSetChanged();

                    tvPaperType.setText(productPaperTypeList.get(position));

                    ((ProductDetailsActivity)productDetailsActivity).getVariation( productDetailsActivity.tvQuantity.getText().toString(),
                            productDetailsActivity.tvCoveroptions.getText().toString(),
                            productDetailsActivity.tvPageincluding.getText().toString(),
                            tvColor.getText().toString(), tvPaperType.getText().toString(),tvSize.getText().toString());
                    dialog.dismiss();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return productPaperTypeList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton chPaperType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chPaperType = itemView.findViewById(R.id.chPaperType);
            chPaperType.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        }
    }
}
