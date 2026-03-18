package com.app.fastprint.ui.category.services;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fastprint.ui.category.aboutus.AboutusActivity;
import com.app.fastprint.ui.chat.chatActivity;
import com.app.fastprint.ui.settings.SettingActivity;
import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.category.services.interfaces.IPServices;
import com.app.fastprint.ui.category.services.interfaces.IServices;
import com.app.fastprint.ui.category.services.presenters.PServices;
import com.app.fastprint.ui.category.services.responseModel.ServicesResponseModel;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServicesActivity extends BaseClass implements IServices {

    ImageView imgBack;
    TextView tvTittle;
    TextView tvServices;
    ImageView imgServices;
    RelativeLayout layoutServices;
    Dialog dialog;
    Context context;
    IPServices ipServices;
    JustifiedTextView tvPrePress;
    JustifiedTextView tvPost;
    JustifiedTextView tvPrePost;
    Button live_chat_option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        context = ServicesActivity.this;
        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        tvServices = findViewById(R.id.tvServices);
        imgServices = findViewById(R.id.imgServices);
        layoutServices = findViewById(R.id.layoutServices);
        tvPrePress = findViewById(R.id.tvPrePress);
        tvPost = findViewById(R.id.tvPost);
        tvPrePost = findViewById(R.id.tvPrePost);
        live_chat_option = findViewById(R.id.live_chat_option);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        live_chat_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServicesActivity.this, chatActivity.class);
                startActivity(intent);
            }
        });

        ipServices = new PServices(this);
        layoutServices.setVisibility(View.GONE);
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipServices.getServices();
        }
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvServices.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(this));

    }



    @Override
    public void successResponseFromPresenter(ServicesResponseModel servicesResponseModel) {
        dialog.dismiss();
        if (servicesResponseModel != null) {
            layoutServices.setVisibility(View.VISIBLE);
            tvPrePress.setText(Html.fromHtml(servicesResponseModel.getData().getPrePress()));
            tvPost.setText(Html.fromHtml(servicesResponseModel.getData().getPost()));
            tvPrePost.setText(Html.fromHtml(servicesResponseModel.getData().getPostPress()));
            if (servicesResponseModel.getData().getImage() != null) {
                Glide.with(context).load(servicesResponseModel.getData().getImage())
                        .placeholder(R.drawable.ic_refresh)
                        .into(imgServices);
            } else {
                Glide.with(context).load(servicesResponseModel.getData().getImage())
                        .placeholder(R.drawable.ic_refresh)
                        .into(imgServices);
            }
        }
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
        layoutServices.setVisibility(View.GONE);
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }
}
