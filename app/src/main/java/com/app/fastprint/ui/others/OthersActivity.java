package com.app.fastprint.ui.others;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.others.interfaces.IOther;
import com.app.fastprint.ui.others.interfaces.IPOther;
import com.app.fastprint.ui.others.presenter.POther;
import com.app.fastprint.ui.others.responseModel.OtherResponseModel;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OthersActivity extends BaseClass implements IOther {

    ImageView imgBack;
    TextView tvTittle;
    TextView tvOption;

    String intentType = "";
    Context context;
    IPOther ipOther;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        tvOption = findViewById(R.id.tvOption);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        context=OthersActivity.this;
        ipOther=new POther(this);
        intentType = getIntent().getStringExtra("intentType");
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipOther.getOther();
        }
        if (intentType.equalsIgnoreCase("privacy_policy")) {
            tvTittle.setText("Privacy Policy");
        } else if (intentType.equalsIgnoreCase("refund_policy")) {
            tvTittle.setText("Refund Policy");
        } else if (intentType.equalsIgnoreCase("terms_and_conditions")) {
            tvTittle.setText("Terms and Conditions");
        }
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));

    }



    @Override
    public void successResponseFromPresenter(OtherResponseModel otherResponseModel) {
        dialog.dismiss();
        if (otherResponseModel.getData()!=null){
            if (intentType.equalsIgnoreCase("privacy_policy")) {
                tvOption.setText((Html.fromHtml(otherResponseModel.getData().getPrivacyPolicy())));
            } else if (intentType.equalsIgnoreCase("refund_policy")) {
                tvOption.setText((Html.fromHtml(otherResponseModel.getData().getRefundPolicy())));
            } else if (intentType.equalsIgnoreCase("terms_and_conditions")) {
                tvOption.setText((Html.fromHtml(otherResponseModel.getData().getTermsAndConditions())));
            }
        }
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
    }
}