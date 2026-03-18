package com.app.fastprint.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.ui.cart.CartActivity;
import com.app.fastprint.ui.cart.responseModel.CartListing;
import com.app.fastprint.ui.product.responseModel.addcartResponseModel;
import com.app.fastprint.ui.productDetails.ProductDetailsActivity;
import com.app.fastprint.utills.cartResponseInterface;
import com.facebook.login.LoginManager;
import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.AccountInformations.AccountInfoActivity;
import com.app.fastprint.ui.changepassword.ChangePasswordActivity;
import com.app.fastprint.ui.main.MainActivity;
import com.app.fastprint.ui.myorders.MyOrdersActivity;
import com.app.fastprint.ui.notification.NotificationActivity;
import com.app.fastprint.ui.others.OthersActivity;
import com.app.fastprint.ui.settings.adapter.SettingsAdapter;
import com.app.fastprint.ui.settings.interfaces.IPSetting;
import com.app.fastprint.ui.settings.interfaces.ISetting;
import com.app.fastprint.ui.settings.presenter.PSetting;
import com.app.fastprint.ui.settings.responseModel.LogoutResponseModel;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseClass implements ISetting {
    Context context;
    SettingsAdapter settingsAdapter;

    ImageView imgBack;
    TextView tvTittle;
    RelativeLayout Accountinfo;
    RelativeLayout Notifications;
    RelativeLayout Myorders;
    RelativeLayout Changepassword;
    RelativeLayout Privacypolicy;
    RelativeLayout Refundpolicy;
    RelativeLayout Termsandconditions;
    RelativeLayout Logout;
    RelativeLayout DeleteAccount;

    IPSetting ipSetting;
    Dialog mdialog;

    Dialog dialog;

    String auth_token = "";
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Bind views
        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        Accountinfo = findViewById(R.id.Accountinfo);
        Notifications = findViewById(R.id.Notifications);
        Myorders = findViewById(R.id.Myorders);
        Changepassword = findViewById(R.id.Changepassword);
        Privacypolicy = findViewById(R.id.Privacypolicy);
        Refundpolicy = findViewById(R.id.Refundpolicy);
        Termsandconditions = findViewById(R.id.Termsandconditions);
        Logout = findViewById(R.id.Logout);
        DeleteAccount = findViewById(R.id.DeleteAccount);

        context = SettingActivity.this;
        ipSetting = new PSetting(this);
        auth_token = AppControler.getInstance(context).getString(AppControler.Key.AUTH_TOKEN);
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        if (auth_token != null && !auth_token.isEmpty()) {
            Accountinfo.setVisibility(View.VISIBLE);
            Myorders.setVisibility(View.VISIBLE);
            Changepassword.setVisibility(View.VISIBLE);
            Logout.setVisibility(View.VISIBLE);
        } else {
            Accountinfo.setVisibility(View.GONE);
            Myorders.setVisibility(View.GONE);
            Changepassword.setVisibility(View.GONE);
            Logout.setVisibility(View.GONE);
        }

        // Set onClickListeners
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Accountinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AccountInfoActivity.class);
                startActivity(intent);
            }
        });

        Notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NotificationActivity.class);
                startActivity(intent);
            }
        });

        Myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyOrdersActivity.class);
                startActivity(intent);
            }
        });

        Changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        Privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OthersActivity.class);
                intent.putExtra("intentType", "privacy_policy");
                startActivity(intent);
            }
        });

        Refundpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OthersActivity.class);
                intent.putExtra("intentType", "refund_policy");
                startActivity(intent);
            }
        });

        Termsandconditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OthersActivity.class);
                intent.putExtra("intentType", "terms_and_conditions");
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAccount();
            }
        });

        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    private void logoutAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogeTheme);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                            mdialog = UtilsAlertDialog.ShowDialog(context);
                            if (auth_token != null) {
                                ipSetting.logout(auth_token);
                                LoginManager.getInstance().logOut();
                            }
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogeTheme);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to delete this record?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                            mdialog = UtilsAlertDialog.ShowDialog(context);
                            if (auth_token != null) {
                                LoginManager.getInstance().logOut();
                                hitApiToDeleteAccount();
                            }
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void successResponseFromPresenter(LogoutResponseModel logoutResponseModel) {
        mdialog.dismiss();
        Toast.makeText(context, "" + logoutResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
        logoutGoogle();
        LoginManager.getInstance().logOut();
        AppControler.getInstance(context).clear();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void logoutGoogle() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        mdialog.dismiss();
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void hitApiToDeleteAccount() {
        dialog = UtilsAlertDialog.ShowDialog(context);
        String user_id = AppControler.getInstance().getString(AppControler.Key.USER_ID, "");
        String device_Token = ""; // AppControler.getInstance().getString(AppControler.Key.DEVICE_TOKEN, "");

        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<LogoutResponseModel> callGenerateToken = apiService.DeleteAccount(auth_token);

        callGenerateToken.enqueue(new Callback<LogoutResponseModel>() {
            @Override
            public void onResponse(Call<LogoutResponseModel> call, Response<LogoutResponseModel> response) {
                if (response.isSuccessful()) {
                    AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
                        @Override
                        public void onDataGot(ArrayList<CartListing> cartListings) {
                            dialog.dismiss();
                            AppControler.getInstance(context).clear();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
