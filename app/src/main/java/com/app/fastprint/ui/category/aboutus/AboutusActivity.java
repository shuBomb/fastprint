package com.app.fastprint.ui.category.aboutus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fastprint.ui.chat.chatActivity;
import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.category.aboutus.interfaces.IAboutus;
import com.app.fastprint.ui.category.aboutus.interfaces.IPAboutus;
import com.app.fastprint.ui.category.aboutus.presenters.PAboutus;
import com.app.fastprint.ui.category.aboutus.responseModel.AboutUsResponseModel;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutusActivity extends BaseClass implements IAboutus {

    IPAboutus ipAboutus;
    Dialog dialog;
    Context context;

    private ImageView imgBack;
    private ImageView imgAboutus;
    private TextView tvTittle;
    private TextView tvHeading;
    private TextView director;
    private JustifiedTextView tvDirector;
    private TextView deputydirector;
    private JustifiedTextView tvDeputyDirector;
    private ImageView imgLinkden;
    private TextView founded;
    private TextView tvfounded;
    private TextView ourMission;
    private JustifiedTextView tvourMission;
    private TextView ourVision;
    private JustifiedTextView tvourVision;
    private TextView ourPromise;
    private JustifiedTextView tvourPromise;
    private TextView ourProduct;
    private JustifiedTextView tvourProduct;
    private RelativeLayout layoutRelative;
    Button live_chat_option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        context = AboutusActivity.this;

        imgBack = findViewById(R.id.imgBack);
        imgAboutus = findViewById(R.id.imgAboutus);
        tvTittle = findViewById(R.id.tvTittle);
        tvHeading = findViewById(R.id.tvHeading);
        director = findViewById(R.id.director);
        tvDirector = findViewById(R.id.tvDirector);
        deputydirector = findViewById(R.id.deputydirector);
        tvDeputyDirector = findViewById(R.id.tvDeputyDirector);
        imgLinkden = findViewById(R.id.imgLinkden);
        founded = findViewById(R.id.founded);
        tvfounded = findViewById(R.id.tvfounded);
        ourMission = findViewById(R.id.ourMission);
        tvourMission = findViewById(R.id.tvourMission);
        ourVision = findViewById(R.id.ourVision);
        tvourVision = findViewById(R.id.tvourVision);
        ourPromise = findViewById(R.id.ourPromise);
        tvourPromise = findViewById(R.id.tvourPromise);
        ourProduct = findViewById(R.id.ourProduct);
        tvourProduct = findViewById(R.id.tvourProduct);
        layoutRelative = findViewById(R.id.layoutRelative);
        live_chat_option = findViewById(R.id.live_chat_option);

        // Set click listeners
        imgBack.setOnClickListener(v -> {
            // Handle the back button click
            finish();
        });

        live_chat_option.setOnClickListener(v -> {
            // Handle the About Us button click
            Intent intent = new Intent(AboutusActivity.this, chatActivity.class);
            startActivity(intent);
        });

        imgLinkden.setOnClickListener(v -> {
            // Handle LinkedIn image click
            openLinkedInPage();
        });


        ipAboutus = new PAboutus(this);
        layoutRelative.setVisibility(View.GONE);
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipAboutus.getAboutUs();
        }
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        director.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        deputydirector.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        founded.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        ourMission.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        ourVision.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        ourPromise.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        ourProduct.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvfounded.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));

    }


    @Override
    public void successResponseFromPresenter(AboutUsResponseModel aboutUsResponseModel) {
        dialog.dismiss();
        layoutRelative.setVisibility(View.VISIBLE);
        if (aboutUsResponseModel != null) {
            tvHeading.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getHeading()));
            tvDirector.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getDirector()));
            tvDeputyDirector.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getDeputyDirector()));
            tvfounded.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getFounded()));
            tvourMission.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getOurMisssion()));
            tvourVision.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getOurVision()));
            tvourPromise.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getOurPromise()));
            tvourProduct.setText(Html.fromHtml(aboutUsResponseModel.getData().getAboutUs().getOurProducts()));

            if (aboutUsResponseModel.getData().getAboutUs().getHeading() != null) {
                Glide.with(context).load(aboutUsResponseModel.getData().getAboutUs().getLingdinImage())
                        .placeholder(R.drawable.ic_linkedin)
                        .into(imgLinkden);
            } else {
                Glide.with(context).load(aboutUsResponseModel.getData().getAboutUs().getLingdinImage())
                        .placeholder(R.drawable.ic_linkedin)
                        .into(imgLinkden);
            }


            if (aboutUsResponseModel.getData().getAboutUs().getHeading() != null) {
                Glide.with(context).load(aboutUsResponseModel.getData().getAboutUs().getImageAboutus())
                        .placeholder(R.drawable.ic_refresh)
                        .into(imgAboutus);
            } else {
                Glide.with(context).load(aboutUsResponseModel.getData().getAboutUs().getImageAboutus())
                        .placeholder(R.drawable.ic_refresh)
                        .into(imgAboutus);
            }
        }
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
        layoutRelative.setVisibility(View.GONE);
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }




    public void openLinkedInPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://add/%@" + "adham-f-dababneh-mba-117b3725"));
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/adham-f-dababneh-mba-117b3725"));
        }
        startActivity(intent);
    }
}
