package com.app.fastprint.ui.main;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.ui.FilePicker.FilePickerDropBoxActivity;
import com.app.fastprint.ui.FilePicker.GoogleDriveActivity;
import com.app.fastprint.ui.cart.responseModel.CartListing;
import com.app.fastprint.ui.chat.chatActivity;
import com.app.fastprint.ui.payment.generateTokens.GenerateTokenResponse;
import com.app.fastprint.utills.cartResponseInterface;
import com.bumptech.glide.Glide;
import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.ui.cart.CartActivity;
import com.app.fastprint.ui.category.aboutus.AboutusActivity;
import com.app.fastprint.ui.category.contactus.ContactUsActivity;
import com.app.fastprint.ui.category.enquiryforms.EnquiryFormsActivity;
import com.app.fastprint.ui.category.extras.ExtrasActivity;
import com.app.fastprint.ui.category.gallery.GalleryActivity;
import com.app.fastprint.ui.category.logo.DefinationofLogoActivity;
import com.app.fastprint.ui.category.services.ServicesActivity;
import com.app.fastprint.ui.login.LoginActivity;
import com.app.fastprint.ui.main.adapter.CategoriesAdapter;
import com.app.fastprint.ui.main.interfaces.ICategories;
import com.app.fastprint.ui.main.interfaces.IPCategories;
import com.app.fastprint.ui.main.presenters.PCategories;
import com.app.fastprint.ui.main.responseModel.MenuListResponseModel;
import com.app.fastprint.ui.product.ProductListingActivity;
import com.app.fastprint.ui.main.adapter.ImageSlidingAdapter;
import com.app.fastprint.ui.settings.SettingActivity;
import com.app.fastprint.ui.socialmedia.SocialMedialActivity;
import com.app.fastprint.utills.AppConstants;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
 import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseClass implements ICategories {

    boolean doubleBackToExitPressedOnce = false;

    private SearchView searchView;
    private TextView tvHome, tvServices,tvDropbox,tvGallery, tvDefinitionlogo, tvContactus, tvEnquiry, tvStore, tvSocialMedia, tvSettings, tvAboutUs, tvExtras, tvCategoreis;
    private SparkButton imgCart;
    private ImageView imgMenu;
    private DrawerLayout drawerLayout;
    private RelativeLayout layoutProfile, layoutGuestUser;
    private CircleImageView ImgUser, ImgGuestUser;
    private TextView tvUserName, tvUserEmail, tvLogin, tvGuestUser;
    private RecyclerView RecyclerCategories;
    private TextView textViewCountCart;
    private CirclePageIndicator indicator;
    Button live_chat_option,img_whatsapp;

    Context context;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;


    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    CategoriesAdapter categoriesAdapter;
    IPCategories ipCategories;

    Dialog dialog;

    ArrayList<MenuListResponseModel.Data.Banner> imageModelArrayList;
    String auth_token = "";
    String user_image = "";
    String user_email = "";
    String user_first_name = "";
    String user_last_name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        searchView = findViewById(R.id.searchView);
        tvHome = findViewById(R.id.tvHome);
        imgMenu = findViewById(R.id.imgMenu);
        tvServices = findViewById(R.id.tvServices);
        tvDefinitionlogo = findViewById(R.id.tvDefinitionlogo);
        tvContactus = findViewById(R.id.tvContactus);
        tvEnquiry = findViewById(R.id.tvEnquiry);
        tvStore = findViewById(R.id.tvStore);
        tvSocialMedia = findViewById(R.id.tvSocialMedia);
        tvSettings = findViewById(R.id.tvSettings);
        tvAboutUs = findViewById(R.id.tvAboutUs);
        tvExtras = findViewById(R.id.tvExtras);
        tvCategoreis = findViewById(R.id.tvCategoreis);
        imgCart = findViewById(R.id.imgCart);
        imgCart = findViewById(R.id.imgCart);
        tvDropbox = findViewById(R.id.tvDropbox);
        tvGallery = findViewById(R.id.tvGallery);
        drawerLayout = findViewById(R.id.drawerLayout);
        layoutProfile = findViewById(R.id.layoutProfile);
        layoutGuestUser = findViewById(R.id.layoutGuestUser);
        ImgUser = findViewById(R.id.ImgUser);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvLogin = findViewById(R.id.tvLogin);
        tvGuestUser = findViewById(R.id.tvGuestUser);
        ImgGuestUser = findViewById(R.id.ImgGuestUser);
        indicator = findViewById(R.id.indicator);
        RecyclerCategories = findViewById(R.id.RecyclerCategories);
        textViewCountCart = findViewById(R.id.textViewCountCart);
        live_chat_option = findViewById(R.id.live_chat_option);
        img_whatsapp = findViewById(R.id.img_whatsapp);



        ipCategories = new PCategories(this);
        auth_token = AppControler.getInstance(context).getString(AppControler.Key.AUTH_TOKEN);
        user_first_name = AppControler.getInstance(context).getString(AppControler.Key.USER_FIRST_NAME);
        user_last_name = AppControler.getInstance(context).getString(AppControler.Key.USER_LAST_NAME);
        user_image = AppControler.getInstance(context).getString(AppControler.Key.USER_IMAGE);
        user_email = AppControler.getInstance(context).getString(AppControler.Key.USER_EMAIL);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.fastprint",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                //System.out.Println("YourKeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        if (auth_token != null && !auth_token.isEmpty()) {
            layoutProfile.setVisibility(View.VISIBLE);
            layoutGuestUser.setVisibility(View.INVISIBLE);
            if (user_image != null) {
                Glide.with(context).load(user_image)
                        .placeholder(R.drawable.ic_guest_user)
                        .into(ImgUser);
            } else {
                Glide.with(context).load(user_image)
                        .placeholder(R.drawable.ic_guest_user)
                        .into(ImgUser);
            }
            tvUserName.setText(user_first_name + " " + user_last_name);
            tvUserEmail.setText(user_email);
        } else {
            layoutGuestUser.setVisibility(View.VISIBLE);
            layoutProfile.setVisibility(View.INVISIBLE);
        }
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipCategories.getMenuList();
        }

        imgCart.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                new Handler().postDelayed(() -> {
                    if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                        //if(!textViewCountCart.getText().toString().equals("0")){
                            Intent intent = new Intent(MainActivity.this, CartActivity.class);
                            startActivity(intent);
                            imgCart.setChecked(false);
                        //}
                    }
                }, 300);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
        viewInitialization();
        generateToken();
        //  dialog.show();

        AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
            @Override
            public void onDataGot(ArrayList<CartListing> cartListings) {
                dialog.hide();
                textViewCountCart.setText(cartListings.size() + "");
                AppControler.getInstance(context).UpdateNotificationReadCount();
            }

            @Override
            public void onError(String error) {

            }
        });

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void viewInitialization() {

        tvHome.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvServices.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvDefinitionlogo.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvContactus.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvEnquiry.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvStore.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvSocialMedia.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvSettings.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvAboutUs.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvAboutUs.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvGuestUser.setTypeface(UtilsFontFamily.typeFaceForRobotoRegular(context));
        tvCategoreis.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvLogin.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));

        searchView.setQueryHint("Search here");
       /* searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCategoreis.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvCategoreis.setVisibility(View.VISIBLE);
                return false;
            }
        });*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null&&categoriesAdapter!=null) {
                    categoriesAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });


        imgMenu.setOnClickListener(v -> {
            openCloseDrawer();
        });
        tvAboutUs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutusActivity.class);
            startActivity(intent);
        });
        tvHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        tvServices.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ServicesActivity.class);
            startActivity(intent);
        });
        tvDefinitionlogo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DefinationofLogoActivity.class);
            startActivity(intent);
        });
        tvContactus.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
            startActivity(intent);
        });

        tvEnquiry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EnquiryFormsActivity.class);
            startActivity(intent);
        });

        tvDropbox.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FilePickerDropBoxActivity.class);
            startActivity(intent);
            openCloseDrawer();
        });

        tvStore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductListingActivity.class);
            startActivity(intent);
            openCloseDrawer();
        });

        tvSocialMedia.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SocialMedialActivity.class);
            startActivity(intent);
            openCloseDrawer();
        });

        tvSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            openCloseDrawer();
        });

        tvGallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
            openCloseDrawer();
        });

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            openCloseDrawer();
        });

        live_chat_option.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, chatActivity.class);
            startActivity(intent);
        });

        tvExtras.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExtrasActivity.class);
            startActivity(intent);
        });

        img_whatsapp.setOnClickListener(v -> {
            whatsappIntent();
        });
    }





    @Override
    public void onBackPressed() {
        drawerLayout.closeDrawer(Gravity.LEFT);

        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.back_agin, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

    private void openCloseDrawer() {

        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);

        } else {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    private void whatsappIntent() {
        String contact = "+962797824070"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception  e) {
            Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void successResponseFromPresenter(MenuListResponseModel menuListResponseModel) {
        dialog.dismiss();
        if (menuListResponseModel != null && menuListResponseModel.getData().getMenu().size() > 0
                && menuListResponseModel.getData().getBanner().size() > 0) {

            updateUI(menuListResponseModel.getData());

        } else {

        }
    }

    private void updateUI(MenuListResponseModel.Data data) {
        updateBannerUI(data);
        udpateMenuListUI(data);


    }

    private void udpateMenuListUI(MenuListResponseModel.Data data) {
        int columns = 2;
        categoriesAdapter = new CategoriesAdapter(this, data.getMenu());
        RecyclerCategories.setLayoutManager(new GridLayoutManager(context, columns));
        RecyclerCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        dialog.dismiss();
        String error_message = message;
        Toast.makeText(context, "" + error_message, Toast.LENGTH_SHORT).show();

    }

    private void updateBannerUI(MenuListResponseModel.Data data) {
        imageModelArrayList = new ArrayList<>();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImageSlidingAdapter(MainActivity.this, data.getBanner()));
        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = imageModelArrayList.size();
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //dialog.show();


        textViewCountCart.setText(AppControler.getInstance(context).getCartList(AppConstants.KEY_CART).size() + "");
//        AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
//            @Override
//            public void onDataGot(ArrayList<CartListing> cartListings) {
//                dialog.hide();
//                textViewCountCart.setText(cartListings.size() + "");
//                //
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });




    }

    private void hitApiForSaveDeviceToken(String auth_token) {
        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<JsonObject> callGenerateToken = apiService.saveDeviceToken(auth_token,"A");

        callGenerateToken.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void generateToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    hitApiForSaveDeviceToken(task.getResult());
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        resetBadgeCounterOfPushMessages();
    }

    private void resetBadgeCounterOfPushMessages() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        }
    }


}
