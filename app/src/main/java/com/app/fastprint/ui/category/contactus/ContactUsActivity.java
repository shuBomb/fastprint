package com.app.fastprint.ui.category.contactus;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.category.contactus.interfaces.IContactUs;
import com.app.fastprint.ui.category.contactus.interfaces.IPContactUs;
import com.app.fastprint.ui.category.contactus.presenters.PContactUs;
import com.app.fastprint.ui.category.contactus.responseModel.ContactUsResponseModel;
import com.app.fastprint.ui.chat.chatActivity;
import com.app.fastprint.ui.main.MainActivity;
import com.app.fastprint.ui.map.MapsActivity;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUsActivity extends BaseClass implements IContactUs {

    private ImageView imgBack;
    private TextView tvTittle;
    private TextView fastprint;
    private TextView tvfastprint;
    private TextView phone;
    private TextView tvphoneNumber;
    private TextView fax;
    private TextView tvFax;
    private TextView mobile;
    private TextView tvMobile;
    private TextView whatsapp;
    private TextView tvWhatsapp;
    private TextView email;
    private TextView tvEmail;
    private TextView skype;
    private TextView tvSkype;
    private TextView ai;
    private TextView tvAi;
    private TextView website;
    private TextView tvWebsite;
    private LinearLayout layoutContact;
    Button live_chat_option;



    Dialog dialog;
    Context context;
    IPContactUs ipContactUs;

    String strAddress = "";
    String latitude = "";
    String longitude = "";
    String webSiteUrl = "";
    String emailAddress = "";
    String whatsappNumbr = "";
    String phoneNumbr = "";
    String faxNumbr = "";
    String mobileNumbr = "";
    String skypeNumbr = "";

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        context = ContactUsActivity.this;

        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        fastprint = findViewById(R.id.fastprint);
        tvfastprint = findViewById(R.id.tvfastprint);
        phone = findViewById(R.id.phone);
        tvphoneNumber = findViewById(R.id.tvphoneNumber);
        fax = findViewById(R.id.fax);
        tvFax = findViewById(R.id.tvFax);
        mobile = findViewById(R.id.mobile);
        tvMobile = findViewById(R.id.tvMobile);
        whatsapp = findViewById(R.id.whatsapp);
        tvWhatsapp = findViewById(R.id.tvWhatsapp);
        email = findViewById(R.id.email);
        tvEmail = findViewById(R.id.tvEmail);
        skype = findViewById(R.id.skype);
        tvSkype = findViewById(R.id.tvSkype);
        ai = findViewById(R.id.ai);
        tvAi = findViewById(R.id.tvAi);
        website = findViewById(R.id.website);
        tvWebsite = findViewById(R.id.tvWebsite);
        layoutContact = findViewById(R.id.layoutContact);
        live_chat_option = findViewById(R.id.live_chat_option);

        imgBack.setOnClickListener(v -> {
            // Handle back button click
            finish();
        });

        tvphoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber(phoneNumbr);
                if (!hasPermissions(ContactUsActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ContactUsActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        });

        tvFax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber(faxNumbr);
                if (!hasPermissions(ContactUsActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ContactUsActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        });

        tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhoneNumber(mobileNumbr);
                if (!hasPermissions(ContactUsActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ContactUsActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        });

        tvWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatsappIntent();
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });

        tvSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(ContactUsActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ContactUsActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
            }
        });

        tvAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLocationDialog();
            }
        });

        tvWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentwebView = new Intent(ContactUsActivity.this, WebViewActivity.class);
                intentwebView.putExtra("webSiteUrl", webSiteUrl);
                intentwebView.putExtra("intent_from", "Contact us");
                startActivity(intentwebView);
            }
        });

        live_chat_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactUsActivity.this, chatActivity.class);
                startActivity(intent);
            }
        });



        ipContactUs = new PContactUs(this);
        initFonts();
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipContactUs.getContactsUs();
        }

    }

    private void initFonts() {

        layoutContact.setVisibility(View.GONE);
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        fastprint.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        website.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        ai.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        skype.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        email.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        whatsapp.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        mobile.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        fax.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        phone.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
    }



    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void whatsappIntent() {
        String contact = whatsappNumbr; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception  e) {
            Toast.makeText(ContactUsActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void sendMail() {


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        try {
            startActivity(Intent.createChooser(intent, "Email via.."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactUsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void successResponseFromPresenter(ContactUsResponseModel contactUsResponseModel) {
        dialog.dismiss();
        if (contactUsResponseModel != null) {
            layoutContact.setVisibility(View.VISIBLE);

            latitude = contactUsResponseModel.getData().getLatitude();
            longitude = contactUsResponseModel.getData().getLongitude();
            strAddress = contactUsResponseModel.getData().getAddress();
            webSiteUrl = contactUsResponseModel.getData().getWebsite();
            emailAddress = contactUsResponseModel.getData().getEmail();
            whatsappNumbr = contactUsResponseModel.getData().getWhatsapp();
            phoneNumbr = contactUsResponseModel.getData().getPhoneNumber();
            faxNumbr = contactUsResponseModel.getData().getFax();
            mobileNumbr = contactUsResponseModel.getData().getMobile();
            skypeNumbr = contactUsResponseModel.getData().getSkype();

            tvfastprint.setText(Html.fromHtml(contactUsResponseModel.getData().getFastPrint()));
            tvphoneNumber.setText(phoneNumbr);
            tvFax.setText(faxNumbr);
            tvMobile.setText(mobileNumbr);
            tvWhatsapp.setText(whatsappNumbr);
            tvEmail.setText(emailAddress);
            tvSkype.setText(skypeNumbr);
            tvAi.setText(contactUsResponseModel.getData().getAlFuhaysJo());
            tvWebsite.setText(webSiteUrl);


        }
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
        layoutContact.setVisibility(View.GONE);
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();
    }

    private void openLocationDialog() {

        dialog = new Dialog(context, R.style.DialogThemes);
        dialog.setContentView(R.layout.layout_location);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        TextView tvShare = (TextView) dialog.findViewById(R.id.tvShare);
        TextView tvMap = (TextView) dialog.findViewById(R.id.tvMap);
        TextView tvDirection = (TextView) dialog.findViewById(R.id.tvDirection);

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double mlatitude = Double.valueOf(latitude);
                Double mlongitude = Double.valueOf(longitude);

                String uri = "http://maps.google.com/maps?saddr=" + mlatitude + "," + mlongitude;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String ShareSub = "Here is my location";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));

                dialog.dismiss();
            }
        });

        tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactUsActivity.this, MapsActivity.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("strAddress", strAddress);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        tvDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", 32.007549, 35.787868);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                        Toast.makeText(context, "Please install a maps application", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
