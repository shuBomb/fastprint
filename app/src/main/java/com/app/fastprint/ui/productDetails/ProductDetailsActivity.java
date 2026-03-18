package com.app.fastprint.ui.productDetails;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.networks.NetworkUtils;
import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.ui.cart.CartActivity;
import com.app.fastprint.ui.cart.responseModel.BuyNowListing;
import com.app.fastprint.ui.cart.responseModel.CartListing;
import com.app.fastprint.ui.login.LoginActivity;
import com.app.fastprint.ui.main.MainActivity;
import com.app.fastprint.ui.notification.notificationResponse.NotificationResponse;
import com.app.fastprint.ui.product.responseModel.addcartResponseModel;
import com.app.fastprint.ui.product.responseModel.cartResponseModel;
import com.app.fastprint.ui.productDetails.adapter.ImageSlidingsAdapter;
import com.app.fastprint.ui.productDetails.adapter.ProductCoverOptionAdapter;
import com.app.fastprint.ui.productDetails.adapter.ProductPageIncludeAdapter;
import com.app.fastprint.ui.productDetails.adapter.ProductPaperTypeAdapter;
import com.app.fastprint.ui.productDetails.adapter.ProductQuantityAdapter;
import com.app.fastprint.ui.productDetails.adapter.SimilarProductsAdapter;
import com.app.fastprint.ui.productDetails.interfaces.IPProductDetails;
import com.app.fastprint.ui.productDetails.interfaces.IProductDetails;
import com.app.fastprint.ui.productDetails.presenter.PProductDetails;
import com.app.fastprint.ui.productDetails.responseModel.ProductDetailsResponseModel;
import com.app.fastprint.ui.productDetails.responseModel.variationResponse.VariationResponseModel;
import com.app.fastprint.ui.reviewListing.ReviewListingActivity;
import com.app.fastprint.ui.uploadFileForm.FormFileUploadActivity;
import com.app.fastprint.utills.AppConstants;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;
import com.app.fastprint.utills.cartResponseInterface;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends BaseClass implements IProductDetails {
    Context context;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    SparkButton imgCart;
    ImageView imgBack,imgBack2;
    TextView tvTittle;
    TextView textViewCountCart;
    CirclePageIndicator indicator;
    TextView tvProductName;
    TextView tvDiscountPrice;
    TextView tvPrice;
    TextView tvPriceRange;
    TextView tvtotalRating;
    TextView tvTotalReview;
    TextView tvDescriptions;
    TextView quantity;
    TextView size;
    TextView color;
    TextView coveroptions;
    TextView pageincluding;
    TextView paperType;

    ImageView removeItemFromCart;
    TextView tvAddItem;
    TextView tvBuyNow;
    RatingBar rating;
    TextView tvAddToCart;
    ImageView addItemFromCart;
    LinearLayout layoutAddtoCart;
    RelativeLayout layoutPageincluding;
    RelativeLayout layoutQuantity;
    RelativeLayout layoutColor;
    RelativeLayout layoutType;
    RelativeLayout layoutBuy;
    RelativeLayout layoutAll;
    RelativeLayout layoutCoveroptions;
    RelativeLayout layoutPaperType;
    LinearLayout layoutBuyNow;
    TextView tvSimilarProducts;
    RecyclerView recyclerSimilarsProduct;


String buttonType="cart";
    public static TextView tvPageincluding;
    public static TextView tvCoveroptions;
    public static TextView tvQuantity;
    public static TextView tvColor;
    public static TextView tvSize;
    public static TextView tvPaperType;


    //private ArrayList<ProductDetailsResponseModel.Data> imageModelArrayList;
    List<String> imageModelArrayList;
    List<String> productQuantityList;
    List<String> productColorList;
    List<String> productSizeList;
    List<String> productPaperTypeList;
    List<String> productPageIncludeList;
    List<String> coverOptionList;
    SimilarProductsAdapter similarProductsAdapter;
    String minPriceRangeUSD = "";
    String maxPriceRangeUSD = "";
    String BasePriceUSD = "";
    Double currencyRateJOD=1.0;
    Double currencyRateUSD=0.0;
    Double currencyRateApplied=1.41422712802887;
    String minPriceRangeApplied = "";
    String maxPriceRangeApplied = "";
    String CurrencySymbol="د.ا";
    String selectedCurrency = "JOD";
    String JOD = "د.ا";
    String USD = "$";
    private int[] myImageList = new int[]{R.drawable.img_1, R.drawable.img_2,
            R.drawable.img_3};

    String product_id = "";
    String currencySymbol = "";
    int variationId = 0;
    IPProductDetails ipProductDetails;
    Dialog dialog;
    public static Dialog productQuantityDialog;
    public static Dialog productPaperTypeDialog;
    public static Dialog productColorDialog;
    public static Dialog productSizeDialog;
    public static Dialog pageIncludeDialog;
    public static Dialog coverOptionDialog;

    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    int i = 0;
    String _stringVal;


    String mquantity = "";
    String mColor = "";
    String mSize = "";
    String mcover_option = "";
    String mpaper_type = "";
    String mpage_including = "";
    String auth_token = "";
    String product_image_url = "";
    String mTotalPrice = "";
    String mTotalDiscount = "";
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        imgBack = findViewById(R.id.imgBack);
        imgBack2 = findViewById(R.id.imgBack2);
        imgCart = findViewById(R.id.imgCart);
        tvTittle = findViewById(R.id.tvTittle);
        textViewCountCart = findViewById(R.id.textViewCountCart);
        indicator = findViewById(R.id.indicator);
        tvProductName = findViewById(R.id.tvProductName);
        tvDiscountPrice = findViewById(R.id.tvDiscountPrice);
        tvPrice = findViewById(R.id.tvPrice);
        tvPriceRange = findViewById(R.id.tvPriceRange);
        tvtotalRating = findViewById(R.id.tvtotalRating);
        tvTotalReview = findViewById(R.id.tvTotalReview);
        tvDescriptions = findViewById(R.id.tvDescriptions);
        quantity = findViewById(R.id.quantity);
        size = findViewById(R.id.type); // Assuming "type" corresponds to size
        color = findViewById(R.id.color);
        coveroptions = findViewById(R.id.coveroptions);
        pageincluding = findViewById(R.id.pageincluding);
        paperType = findViewById(R.id.PaperTypelbl);

        removeItemFromCart = findViewById(R.id.remove_item_from_cart);
        tvAddItem = findViewById(R.id.tvAddItem);
        tvBuyNow = findViewById(R.id.tvBuyNow);
        rating = findViewById(R.id.rating);
        tvAddToCart = findViewById(R.id.tvAddToCart);
        addItemFromCart = findViewById(R.id.add_item_from_cart);
        layoutAddtoCart = findViewById(R.id.layoutAddtoCart);
        layoutPageincluding = findViewById(R.id.layoutPageincluding);
        layoutQuantity = findViewById(R.id.layoutQuantity);
        layoutColor = findViewById(R.id.layoutColor);
        layoutType = findViewById(R.id.layoutType);
        layoutBuy = findViewById(R.id.layoutBuy);
        layoutAll = findViewById(R.id.layoutAll);
        layoutCoveroptions = findViewById(R.id.layoutCoveroptions);
        layoutPaperType = findViewById(R.id.layoutPaperType);
        layoutBuyNow = findViewById(R.id.layoutBuyNow);
        tvSimilarProducts = findViewById(R.id.tvSimilarProducts);

        tvPageincluding = (TextView) findViewById(R.id.tvPageincluding);
        tvCoveroptions = (TextView) findViewById(R.id.tvCoveroptions);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        tvColor = (TextView) findViewById(R.id.tvColor);
        tvSize = (TextView) findViewById(R.id.tvType);
        tvPaperType = (TextView) findViewById(R.id.tvPaperType);

        recyclerSimilarsProduct = findViewById(R.id.recyclerSimilarsProduct);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTaskRoot()) {
                    startActivity(new Intent(ProductDetailsActivity.this, MainActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        });

        imgBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoScreenVar = new Intent(ProductDetailsActivity.this, MainActivity.class);
                gotoScreenVar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gotoScreenVar);
            }
        });

        removeItemFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Decreasing value...");
                if (i > 0) {
                    i = i - 1;
                    _stringVal = String.valueOf(i);
                    tvAddItem.setText(_stringVal);
                } else {
                    Log.d("src", "Value can't be less than 0");
                    Toast.makeText(ProductDetailsActivity.this, "Value can't be less than 0", Toast.LENGTH_SHORT).show();
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(50);
                }
            }
        });

        addItemFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("src", "Increasing value...");
                i = i + 1;
                _stringVal = String.valueOf(i);
                tvAddItem.setText(_stringVal);
            }
        });

        layoutAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mquantity.isEmpty()) {
                    Toast.makeText(ProductDetailsActivity.this, "Product doesn't have quantity. Please specify quantity.", Toast.LENGTH_SHORT).show();
                } else {
                    buttonType = "cart";
                    addProductToCart();
                }
            }
        });

        layoutBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth_token != null && !auth_token.isEmpty()) {
                    buttonType = "buynow";
                    addProductToCart();
                    // Uncomment if needed
                    // imgCart.playAnimation();
                    // saveForBuyNow();
                    // Intent in2 = new Intent(ProductDetailsActivity.this, FormFileUploadActivity.class);
                    // in2.putExtra("intent_From", "productdetails");
                    // startActivity(in2);
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "You must be logged in to add to cart", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, LoginActivity.class);
                    intent.putExtra("intent_type", "from_product_details");
                    intent.putExtra("product_id", product_id);
                    startActivity(intent);
                }
            }
        });

        tvTotalReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in3 = new Intent(ProductDetailsActivity.this, ReviewListingActivity.class);
                in3.putExtra("product_id", product_id);
                startActivity(in3);
            }
        });

        tvPageincluding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPageincluding();
            }
        });

        tvCoveroptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCoveroptions();
            }
        });

        tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogQuantity();
            }
        });

        tvPaperType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPaperType();
            }
        });

        tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogColor();
            }
        });

        tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSize();
            }
        });


        context = ProductDetailsActivity.this;
        auth_token = AppControler.getInstance(context).getString(AppControler.Key.AUTH_TOKEN);
        ipProductDetails = new PProductDetails(this);
        product_id = getIntent().getStringExtra("product_id");
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            dialog = UtilsAlertDialog.ShowDialog(context);
            if (product_id == null && product_id.equals("")) {
                Toast.makeText(context, "Invalid product id", Toast.LENGTH_SHORT).show();
            } else {
                ipProductDetails.getProductDetails(product_id);
            }

        }

        progressDialog = UtilsAlertDialog.progressDialog(context);
        Switch onOffSwitch = (Switch)  findViewById(R.id.currencySwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);

                if (isChecked){
                    CurrencySymbol=USD;
                    selectedCurrency="USD";
                    currencyRateApplied=currencyRateJOD;

                }
                else {
                    CurrencySymbol=JOD;
                    selectedCurrency="JOD";
                    currencyRateApplied=currencyRateUSD;
                }

                Double value =0.0;
                try {

                    value = Double.parseDouble(BasePriceUSD);
                    value=value / currencyRateApplied;
                    String strV= String.format("%.2f", value);
                 //   String strV= String.valueOf(value);
                    tvPrice.setText(strV + " " + CurrencySymbol);

                } catch (NumberFormatException e) {
                    // p did not contain a valid double
                }

                Double mvalue =0.0;
                try {

                    mvalue = Double.parseDouble(minPriceRangeUSD);
                    mvalue=mvalue / currencyRateApplied;
                    minPriceRangeApplied= String.format("%.2f", mvalue);


                } catch (NumberFormatException e) {
                    // p did not contain a valid double
                }
                Double mxvalue =0.0;
                try {
                    mxvalue = Double.parseDouble(maxPriceRangeUSD);
                    mxvalue=mxvalue / currencyRateApplied;
                    maxPriceRangeApplied= String.format("%.2f", mxvalue);


                } catch (NumberFormatException e) {
                    // p did not contain a valid double
                }

                reloadProductDetails();
                reloadSimilarProduct();
            }

        });

        viewInitialization();
    }

    private void viewInitialization() {


        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvSimilarProducts.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvProductName.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvDiscountPrice.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvPrice.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        quantity.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        color.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        size.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        coveroptions.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        pageincluding.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        paperType.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvAddToCart.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(context));
        tvBuyNow.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(context));

        imgCart.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                new Handler().postDelayed(() -> {
                    if (NetworkUtils.isNetworkConnectionAvailable(context)) {
                       // if(!textViewCountCart.getText().toString().equals("0")){
                            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                            startActivity(intent);
                            imgCart.setChecked(false);
                       // }
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

        layoutBuy.setVisibility(View.GONE);
        layoutAll.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewCountCart.setText( AppControler.getInstance(context).getCartList(AppConstants.KEY_CART).size() + "");
//        dialog.show();
//        AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
//            @Override
//            public void onDataGot(ArrayList<CartListing> cartListings) {
//                dialog.hide();
//                textViewCountCart.setText(cartListings.size() + "");
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//        });


    }


    private void addProductToCart() {

        ArrayList<CartListing> cartListings = AppControler.getInstance(context).getCartList(AppConstants.KEY_CART);
        int i = cartListings.size()+1;
        CartListing cartListing = new CartListing(String.valueOf(i),product_id,
                tvProductName.getText().toString(),
                tvQuantity.getText().toString()
                , tvCoveroptions.getText().toString(),
                pageincluding.getText().toString(),
                mTotalPrice,
                mTotalDiscount,
                product_image_url,
                tvtotalRating.getText().toString(),
                rating.getRating(), variationId, currencySymbol);

        cartListings.add(cartListing);
        AppControler.getInstance(context).addProductToCart(cartListings, AppConstants.KEY_CART);
        imgCart.playAnimation();

        if(buttonType=="cart"){
            Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show();
        }

        hitApiToAddProductIntoCart(tvQuantity.getText().toString(),String.valueOf(variationId),product_id);


//        AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
//            @Override
//            public void onDataGot(ArrayList<CartListing> cartListings) {
//                progressDialog.hide();
//
//
//                CartListing cartListing = new CartListing(product_id,
//                        tvProductName.getText().toString(),
//                        tvQuantity.getText().toString()
//                        , tvCoveroptions.getText().toString(),
//                        pageincluding.getText().toString(),
//                        mTotalPrice,
//                        mTotalDiscount,
//                        product_image_url,
//                        tvtotalRating.getText().toString(),
//                        rating.getRating(), variationId, currencySymbol);
//
//                cartListings.add(cartListing);
//                AppControler.getInstance(context).addProductToCart(cartListings, AppConstants.KEY_CART);
//
//
//                Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show();
//                hitApiToAddProductIntoCart(tvQuantity.getText().toString(),String.valueOf(variationId),product_id);
//            }
//        });

    }
    private void hitApiToAddProductIntoCart(String quantity,String variationId,String product_id) {
        dialog.show();
       // progressDialog.show();
       String user_id= AppControler.getInstance().getString(AppControler.Key.USER_ID,"");
        String device_Token = ""; //AppControler.getInstance().getString(AppControler.Key.DEVICE_TOKEN, "");
//        if (user_id.isEmpty() && device_Token.isEmpty()) {
//            return;
//        }

        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<addcartResponseModel> callGenerateToken = apiService.AddProductToCartAPICall(user_id,device_Token,product_id,quantity,variationId);

        callGenerateToken.enqueue(new Callback<addcartResponseModel>() {
            @Override
            public void onResponse(Call<addcartResponseModel> call, Response<addcartResponseModel> response) {
                if (response.isSuccessful()) {
                  //  imgCart.playAnimation();

                    AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
                        @Override
                        public void onDataGot(ArrayList<CartListing> cartListings) {
                            dialog.dismiss();
                            textViewCountCart.setText(cartListings.size() + "");
                        if(buttonType=="buynow"){
                            Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                            startActivity(intent);
                        }

                        }

                        @Override
                        public void onError(String error) {
                            dialog.dismiss();

                        }
                    });

                } else
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


               // progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<addcartResponseModel> call, Throwable t) {
            //    progressDialog.dismiss();
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void saveForBuyNow() {
        ArrayList<BuyNowListing> buyNowListings = new ArrayList<>();

        BuyNowListing buyNowListing = new BuyNowListing(product_id,
                tvProductName.getText().toString(),
                tvQuantity.getText().toString()
                , tvCoveroptions.getText().toString(),
                pageincluding.getText().toString(),
                mTotalPrice,
                mTotalDiscount,
                product_image_url,
                tvtotalRating.getText().toString(),
                rating.getRating(), currencySymbol);

        buyNowListings.add(buyNowListing);
        AppControler.getInstance(this).addProductToBuyNow(buyNowListings, AppConstants.KEY_BUY_NOW);
    }

    private void dialogPageincluding() {

        pageIncludeDialog = new Dialog(ProductDetailsActivity.this);
        pageIncludeDialog.setContentView(R.layout.dialog_page_include);
        pageIncludeDialog.setCancelable(true);
        RecyclerView recyclerPageInclude = pageIncludeDialog.findViewById(R.id.recyclerPageInclude);
        ProductPageIncludeAdapter slidesAdapter = new ProductPageIncludeAdapter(context, productPageIncludeList, ProductDetailsActivity.this);
        recyclerPageInclude.setLayoutManager(new LinearLayoutManager(this));
        recyclerPageInclude.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerPageInclude.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerPageInclude.addItemDecoration(divider);
        pageIncludeDialog.show();
    }

    private void dialogCoveroptions() {
        coverOptionDialog = new Dialog(ProductDetailsActivity.this);
        coverOptionDialog.setContentView(R.layout.dialog_cover_options);
        coverOptionDialog.setCancelable(true);
        RecyclerView recyclerCoverPage = coverOptionDialog.findViewById(R.id.recyclerCoverPage);
        ProductCoverOptionAdapter slidesAdapter = new ProductCoverOptionAdapter(context, coverOptionList, ProductDetailsActivity.this);
        recyclerCoverPage.setLayoutManager(new LinearLayoutManager(this));
        recyclerCoverPage.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerCoverPage.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerCoverPage.addItemDecoration(divider);
        coverOptionDialog.show();
    }
    private void dialogPaperType() {
        productPaperTypeDialog = new Dialog(ProductDetailsActivity.this);
        productPaperTypeDialog.setContentView(R.layout.dialog_paper_type);
        productPaperTypeDialog.setCancelable(true);
        RecyclerView recyclerQuantity = productPaperTypeDialog.findViewById(R.id.recyclerPaperType);
        ProductPaperTypeAdapter slidesAdapter = new ProductPaperTypeAdapter(context,
                tvPaperType, productPaperTypeList, ProductDetailsActivity.this,
                productPaperTypeDialog);
        recyclerQuantity.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuantity.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerQuantity.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerQuantity.addItemDecoration(divider);
        productPaperTypeDialog.show();
    }
    private void dialogQuantity() {
        productQuantityDialog = new Dialog(ProductDetailsActivity.this);
        productQuantityDialog.setContentView(R.layout.dialog_quantity);
        productQuantityDialog.setCancelable(true);
        RecyclerView recyclerQuantity = productQuantityDialog.findViewById(R.id.recyclerQuantity);
        ProductQuantityAdapter slidesAdapter = new ProductQuantityAdapter(context,
                tvQuantity, productQuantityList, ProductDetailsActivity.this,
                productQuantityDialog);
        recyclerQuantity.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuantity.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerQuantity.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerQuantity.addItemDecoration(divider);
        productQuantityDialog.show();
    }

    private void dialogColor() {
        productColorDialog = new Dialog(ProductDetailsActivity.this);
        productColorDialog.setContentView(R.layout.dialog_quantity);
        productColorDialog.setCancelable(true);
        RecyclerView recyclerQuantity = productColorDialog.findViewById(R.id.recyclerQuantity);
        ProductQuantityAdapter slidesAdapter = new ProductQuantityAdapter(context, tvColor,
                productColorList, ProductDetailsActivity.this,productColorDialog);
        recyclerQuantity.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuantity.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerQuantity.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerQuantity.addItemDecoration(divider);
        productColorDialog.show();
    }

    private void dialogSize() {
        productSizeDialog = new Dialog(ProductDetailsActivity.this);
        productSizeDialog.setContentView(R.layout.dialog_quantity);
        productSizeDialog.setCancelable(true);
        RecyclerView recyclerQuantity = productSizeDialog.findViewById(R.id.recyclerQuantity);
        ProductQuantityAdapter slidesAdapter = new ProductQuantityAdapter(context, tvSize,
                productSizeList, ProductDetailsActivity.this,productSizeDialog);
        recyclerQuantity.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuantity.setAdapter(slidesAdapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerQuantity.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerQuantity.addItemDecoration(divider);
        productSizeDialog.show();
    }

    @Override
    public void successResponseFromPresenter(ProductDetailsResponseModel productDetailsResponseModel) {
        dialog.dismiss();
        if (productDetailsResponseModel.getData() != null) {
            layoutBuy.setVisibility(View.VISIBLE);
            layoutAll.setVisibility(View.VISIBLE);

            if (productDetailsResponseModel.getData().getProductImages().size() > 0)
                product_image_url = productDetailsResponseModel.getData().getProductImages().get(0);

            imageModelArrayList = productDetailsResponseModel.getData().getProductImages();
            currencySymbol = Html.fromHtml(productDetailsResponseModel.getData().getProductDetail().get(0).getCurrencySymbol()).toString();

            if (productDetailsResponseModel.getData().getQuantity() != null && productDetailsResponseModel.getData().getQuantity().size() > 0) {
                layoutQuantity.setVisibility(View.VISIBLE);
                productQuantityList = productDetailsResponseModel.getData().getQuantity();
                Collections.sort(productQuantityList, new NumericalStringComparator());
                tvQuantity.setText(productDetailsResponseModel.getData().getQuantity().get(0));
                mquantity = tvQuantity.getText().toString();

            } else {
                layoutQuantity.setVisibility(View.GONE);

            }
            if (productDetailsResponseModel.getData().getColor() != null && productDetailsResponseModel.getData().getColor().size() > 0) {
                layoutColor.setVisibility(View.VISIBLE);
                productColorList = productDetailsResponseModel.getData().getColor();
                tvColor.setText(productDetailsResponseModel.getData().getColor().get(0));
                mColor = tvColor.getText().toString();

            } else {
                layoutColor.setVisibility(View.GONE);

            }
            if (productDetailsResponseModel.getData().getSize() != null && productDetailsResponseModel.getData().getSize().size() > 0) {
                layoutType.setVisibility(View.VISIBLE);
                productSizeList = productDetailsResponseModel.getData().getSize();
                Collections.sort(productSizeList, new NumericalStringComparator());
                tvSize.setText(productDetailsResponseModel.getData().getSize().get(0));
                mSize = tvSize.getText().toString();

            } else {
                layoutType.setVisibility(View.GONE);

            }


            if (productDetailsResponseModel.getData().getPages() != null && productDetailsResponseModel.getData().getPages().size() > 0) {
                layoutPageincluding.setVisibility(View.VISIBLE);
                productPageIncludeList = productDetailsResponseModel.getData().getPages();
                Collections.sort(productPageIncludeList, new NumericalStringComparator());
                tvPageincluding.setText(productDetailsResponseModel.getData().getPages().get(0));
                mpage_including = tvPageincluding.getText().toString();

            } else {
                layoutPageincluding.setVisibility(View.GONE);

            }

            HashMap<String,Object> attributes= productDetailsResponseModel.getData().getAttributes();
            ArrayList<String> arrPaperType;
            if(attributes.get("Type of Paper") != null){
                arrPaperType= ((ArrayList<String>) attributes.get("Type of Paper"));
            }
            else
            {
                arrPaperType= ((ArrayList<String>) attributes.get("pa_type-of-paper"));
            }

            ArrayList<String> arrCover;
            if(attributes.get("pa_cover-options") != null){
                arrCover= ((ArrayList<String>) attributes.get("pa_cover-options"));
            }
            else
            {
                arrCover= ((ArrayList<String>) attributes.get("Cover options"));
            }

            ArrayList<String> arrColor= ((ArrayList<String>) attributes.get("pa_color"));
            ArrayList<String> arrSize= ((ArrayList<String>) attributes.get("pa_size"));
          //  ArrayList<String> arrQuantity= ((ArrayList<String>) attributes.get("pa_quantity"));
            ArrayList<String> arrQuantity;
            if(attributes.get("pa_quantity") != null){
                arrQuantity= ((ArrayList<String>) attributes.get("pa_quantity"));
            }
            else
            {
                arrQuantity= ((ArrayList<String>) attributes.get("Quantity"));
            }
            ArrayList<String> arrPages;
            try {
                arrPages=((ArrayList<String>) attributes.get("pa_number-of-pages"));
            }
            catch (Exception e){
                arrPages=null;
            }



            if (arrPages != null && productDetailsResponseModel.getData().getAttributes() != null &&  arrPages.size() > 0) {
                layoutPageincluding.setVisibility(View.VISIBLE);
                productPageIncludeList = arrPages;
                Collections.sort(productPageIncludeList, new NumericalStringComparator());
                tvPageincluding.setText(productPageIncludeList.get(0));
                mpage_including = tvPageincluding.getText().toString();

            } else {
                layoutPageincluding.setVisibility(View.GONE);

            }
            if (arrCover != null && productDetailsResponseModel.getData().getAttributes() != null &&  arrCover.size() > 0) {
                layoutCoveroptions.setVisibility(View.VISIBLE);
                coverOptionList = arrCover;
              //  Collections.sort(coverOptionList, new NumericalStringComparator());
                tvCoveroptions.setText(coverOptionList.get(0));
                mcover_option = tvCoveroptions.getText().toString();

            } else {
                layoutCoveroptions.setVisibility(View.GONE);

            }


//            if (productDetailsResponseModel.getData().getAttributes() != null && productDetailsResponseModel.getData().getAttributes().get("Type of Paper").size() > 0) {
//                layoutPageincluding.setVisibility(View.VISIBLE);
//                productPageIncludeList = productDetailsResponseModel.getData().getPages();
//                Collections.sort(productPageIncludeList, new NumericalStringComparator());
//                tvPageincluding.setText(productDetailsResponseModel.getData().getPages().get(0));
//                mpage_including = tvPageincluding.getText().toString();
//
//            } else {
//                layoutPageincluding.setVisibility(View.GONE);
//
//            }
            if (arrPaperType !=null && productDetailsResponseModel.getData().getAttributes() != null && arrPaperType.size() > 0) {
                layoutPaperType.setVisibility(View.VISIBLE);
                productPaperTypeList = arrPaperType;
                tvPaperType.setText(productPaperTypeList.get(0));
                mpaper_type = tvPaperType.getText().toString();
            } else {
                layoutPaperType.setVisibility(View.GONE);
            }
            if (arrColor !=null && productDetailsResponseModel.getData().getAttributes() != null && arrColor.size() > 0) {
                layoutColor.setVisibility(View.VISIBLE);
                productColorList =arrColor;
                tvColor.setText(arrColor.get(0));
                mColor = tvColor.getText().toString();

            } else {
                layoutColor.setVisibility(View.GONE);

            }
            if (arrSize !=null &&  productDetailsResponseModel.getData().getAttributes() != null && arrSize.size() > 0) {
                layoutType.setVisibility(View.VISIBLE);
                productSizeList = arrSize;
               // Collections.sort(productSizeList, new NumericalStringComparator());
                tvSize.setText(productSizeList.get(0));
                mSize = tvSize.getText().toString();

            } else {
                layoutType.setVisibility(View.GONE);

            }
            if (arrQuantity !=null && productDetailsResponseModel.getData().getAttributes() != null && arrQuantity.size() > 0) {
                layoutQuantity.setVisibility(View.VISIBLE);
                productQuantityList = arrQuantity;
                Collections.sort(productQuantityList, new NumericalStringComparator());
                tvQuantity.setText(productQuantityList.get(0));
                mquantity = tvQuantity.getText().toString();

            } else {
                layoutQuantity.setVisibility(View.GONE);

            }
            getVariation(tvQuantity.getText().toString(),
                    tvCoveroptions.getText().toString(), tvPageincluding.getText().toString()
            ,tvColor.getText().toString(), tvPaperType.getText().toString(),tvSize.getText().toString());

            updateProductBanner(productDetailsResponseModel.getData());
            updateProductDetails(productDetailsResponseModel.getData());
            updateSimilarProduct(productDetailsResponseModel.getData());
        } else {
            layoutBuy.setVisibility(View.GONE);
            layoutAll.setVisibility(View.GONE);
        }
    }

    public class NumericalStringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            if (s1.endsWith("-pages")) {
                s1 = s1.replaceAll("-pages", "");
                s2 = s2.replaceAll("-pages", "");
            }
            int i1 = Integer.parseInt(s1.split(" ")[0]);
            int i2 = Integer.parseInt(s2.split(" ")[0]);
            int cmp = Integer.compare(i1, i2);
            if (cmp != 0) {
                return cmp;
            }
            return s1.compareTo(s2);
        }
    }

    @Override
    public void errorResponseFromPresenter(String message) {
        layoutBuy.setVisibility(View.GONE);
        layoutAll.setVisibility(View.GONE);
        dialog.dismiss();
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successPriceResponseFromPresenter(VariationResponseModel priceResponseModel) {
        dialog.dismiss();

        currencySymbol = Html.fromHtml(priceResponseModel.getData().getCurrencySymbol()).toString();

        variationId = priceResponseModel.getData().getVariationId();

        String getProductPrice = priceResponseModel.getData().getVariationPrice();
        tvPrice.setText(USD+ " " + getProductPrice);
        mTotalPrice=getProductPrice;

        BasePriceUSD=getProductPrice;

        if(selectedCurrency == "JOD"){
            currencyRateApplied=currencyRateUSD;
        }
        Double baseP=Double.parseDouble(BasePriceUSD);
        baseP=baseP/currencyRateApplied;
        String strV= String.format("%.2f", baseP);
        tvPrice.setText(CurrencySymbol+ " " + strV);


       // tvPrice.setText(Html.fromHtml(priceResponseModel.getData().getCurrencySymbol() + getProductPrice));
    }

    @Override
    public void errorPriceResponseFromPresenter(String message) {
        dialog.dismiss();
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    private void updateSimilarProduct(ProductDetailsResponseModel.Data data) {
        similarProductsAdapter = new SimilarProductsAdapter(context, data.getSimilar(),selectedCurrency,currencyRateApplied,JOD,USD);
        recyclerSimilarsProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerSimilarsProduct.setAdapter(similarProductsAdapter);
    }
    private void reloadSimilarProduct() {
        if(similarProductsAdapter != null){

            similarProductsAdapter.updateparsm(selectedCurrency,currencyRateApplied,JOD,USD);
        }

    }


    private void updateProductDetails(ProductDetailsResponseModel.Data data) {
        String cJOD = data.getProductDetail().get(0).getCurrencyRateJOD();
        currencyRateJOD =  Double.parseDouble(cJOD);
        String cUSD = data.getProductDetail().get(0).getCurrencyRate();
        currencyRateUSD =  Double.parseDouble(cUSD);
        currencyRateApplied=currencyRateUSD;

        minPriceRangeUSD = data.getProductDetail().get(0).getMin_price();
        maxPriceRangeUSD = data.getProductDetail().get(0).getMax_price();

         Double minPRangeUSD = Double.parseDouble(minPriceRangeUSD);

         minPRangeUSD=minPRangeUSD/currencyRateApplied;
        String strVM= String.format("%.2f", minPRangeUSD);
        minPriceRangeApplied= strVM;

        Double maxPRangeUSD = Double.parseDouble(maxPriceRangeUSD);
        maxPRangeUSD=maxPRangeUSD/currencyRateApplied;
        String strV= String.format("%.2f", maxPRangeUSD);
        maxPriceRangeApplied= strV;

        tvPrice.setText(CurrencySymbol + " " +
                  data.getProductDetail().get(0).getProductPrice());

        tvProductName.setText(data.getProductDetail().get(0).getProductName().toUpperCase());
        tvDiscountPrice.setPaintFlags(tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvDiscountPrice.setText(Html.fromHtml(data.getProductDetail().get(0).getCurrencySymbol())
                + data.getProductDetail().get(0).getProductRegularPrice());
        mTotalDiscount=data.getProductDetail().get(0).getProductRegularPrice();
      //  tvPrice.setText(Html.fromHtml(data.getProductDetail().get(0).getCurrencySymbol()) +
              //  data.getProductDetail().get(0).getProductPrice());

       String pRange = minPriceRangeApplied + " " + CurrencySymbol + " - " + maxPriceRangeApplied + " " + CurrencySymbol;

        tvPriceRange.setText(pRange);

//        tvPriceRange.setText(Html.fromHtml(data.getProductDetail().get(0).getCurrencySymbol()) +
//                data.getProductDetail().get(0).getMin_price()
//                + " - " + Html.fromHtml(data.getProductDetail().get(0).getCurrencySymbol()) +
//                data.getProductDetail().get(0).getMax_price());

        tvDescriptions.setText((Html.fromHtml(data.getProductDetail().get(0).getDescription())));
        String total_reviews = String.valueOf(data.getProductDetail().get(0).getReviewCount());
        tvTotalReview.setText(total_reviews + " " + "Customer Reviews");
        tvtotalRating.setText("(" + data.getProductDetail().get(0).getAverage() + ")");
        float ratingCount = data.getProductDetail().get(0).getRatingCount();
        rating.setRating(ratingCount);
    }
    private void reloadProductDetails() {

        String pRange = minPriceRangeApplied + " " + CurrencySymbol + " - " + maxPriceRangeApplied + " " + CurrencySymbol;
        tvPriceRange.setText(pRange);
    }


    private void updateProductBanner(ProductDetailsResponseModel.Data data) {
        imageModelArrayList = new ArrayList<>();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImageSlidingsAdapter(ProductDetailsActivity.this, data.getProductImages()));
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
        // timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
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
                Log.d("state++", "state" + pos);
            }
        });
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            ProductDetailsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mPager.getCurrentItem() < imageModelArrayList.size() - 1) {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);

                    } else {
                        mPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void getVariation(String mquantity, String mcover_option, String mpage_including,
                             String color,String mpaperType,String msize) {
        if (NetworkUtils.isNetworkConnectionAvailable(context)) {
            color = color.toLowerCase().replaceAll(" ","-");
            dialog = UtilsAlertDialog.ShowDialog(context);
            ipProductDetails.getPrice(product_id, mquantity, mpage_including,mpaperType, mcover_option,
                    color,msize);
        }
    }
}
