package com.app.fastprint.ui.cart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.ui.cart.adapters.CartItemAdapter;
import com.app.fastprint.ui.cart.couponsResponse.CouponsResponse;
import com.app.fastprint.ui.cart.couponsResponse.DataItem;
import com.app.fastprint.ui.cart.interfaces.ICurrencyInfo;
import com.app.fastprint.ui.cart.responseModel.CartListing;
import com.app.fastprint.ui.cart.responseModel.Currencies;
import com.app.fastprint.ui.cart.responseModel.CurrencyRateResponseModel;
import com.app.fastprint.ui.cart.responseModel.Jod;
import com.app.fastprint.ui.cart.responseModel.Usd;
import com.app.fastprint.ui.chat.chatActivity;
import com.app.fastprint.ui.login.LoginActivity;
import com.app.fastprint.ui.notification.notificationResponse.NotificationResponse;
import com.app.fastprint.ui.payment.generateTokens.GenerateTokenResponse;
import com.app.fastprint.ui.product.ProductListingActivity;
import com.app.fastprint.ui.product.responseModel.addcartResponseModel;
import com.app.fastprint.ui.product.responseModel.cartResponseModel;
import com.app.fastprint.ui.uploadFileForm.FormFileUploadActivity;
import com.app.fastprint.utills.AppConstants;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;
import com.app.fastprint.utills.cartResponseInterface;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseClass implements CartItemAdapter.OnDeleteCartListener {

    private ImageView imgBack;
    private TextView tvTittle;
    private TextView tvyouhave;
    private TextView tvItems;
    private RecyclerView recyclerCartItems;
    private TextView subtotal;
    private TextView tvSubtotal;
    private TextView shipping;
    private TextView tvTax;
    private TextView tvshipping;
    private TextView total;
    private TextView tvtotal;
    private TextView tvUploadFile;
    private TextView tvApplyCoupon;
    private TextView tvContinueshopping;
    private LinearLayout linearLayoutEmptyCart;
    private RelativeLayout relativeLayoutCartMain;
    private EditText editTextCoupon;
    Button live_chat_option;


    Context context;
    CartItemAdapter adapter;
    CartItemAdapter.OnDeleteCartListener onDeleteCartListener;
    private double tax = 0.00;
    private double subtotalAmount = 0.00;
    private final double shipping_fee = 0.00;
    private String consumer_key, consumer_secret,currencySymbol="";
    private ArrayList<CartListing> cartListingsGlobal;
    private Dialog progressDialog;
    private ArrayList<DataItem> couponsList = new ArrayList<>();
    private DataItem selectedCoupon;
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
    String sJOD = "د.ا";
    String sUSD = "$";
    String subTotalGlobal="";
    String TotalGlobal="";
    String FeeGlobal="";
    String TaxGlobal="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        tvyouhave = findViewById(R.id.tvyouhave);
        tvItems = findViewById(R.id.tvItems);
        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        subtotal = findViewById(R.id.subtotal);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        shipping = findViewById(R.id.shipping);
        tvTax = findViewById(R.id.tvTax);
        tvshipping = findViewById(R.id.tvshipping);
        total = findViewById(R.id.total);
        tvtotal = findViewById(R.id.tvtotal);
        tvUploadFile = findViewById(R.id.tvUploadFile);
        tvApplyCoupon = findViewById(R.id.tvApplyCoupon);
        tvContinueshopping = findViewById(R.id.tvContinueshopping);
        linearLayoutEmptyCart = findViewById(R.id.linearLayoutEmptyCart);
        relativeLayoutCartMain = findViewById(R.id.relativeLayoutCartMain);
        editTextCoupon = findViewById(R.id.editTextCoupon);
        live_chat_option = findViewById(R.id.live_chat_option);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvContinueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ProductListingActivity.class);
                startActivity(intent);
            }
        });

        tvApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCoupon.getText().toString().length() > 0) {
                    checkIfCouponValidate();
                }
            }
        });

        tvUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currency = AppControler.getInstance(CartActivity.this).getString(AppControler.Key.Currency);
                String auth_token = AppControler.getInstance(CartActivity.this).getString(AppControler.Key.AUTH_TOKEN);
                if (auth_token != null && !auth_token.isEmpty()) {
                    AppControler.getInstance(CartActivity.this).put(AppControler.Key.CART_TOTAL, tvtotal.getText().toString()
                            .replaceAll(cartListingsGlobal.get(0).getCurrencySymbol(), ""));
                    Intent intent1 = new Intent(CartActivity.this, FormFileUploadActivity.class);
                    intent1.putExtra("intent_From", AppConstants.KEY_CART);
                    startActivity(intent1);
                } else {
                    Toast.makeText(CartActivity.this, "You must be logged in to proceed to checkout", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(CartActivity.this, LoginActivity.class);
                    intent1.putExtra("intent_type", "from_cart_details");
                    startActivity(intent1);
                }
            }
        });

        live_chat_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(CartActivity.this, chatActivity.class);
                startActivity(intent2);
            }
        });


        context = CartActivity.this;
        onDeleteCartListener = this;
        AppControler.getInstance(this).put(AppConstants.KEY_COUPON_ID, "");
        progressDialog = UtilsAlertDialog.progressDialog(context);

        tvItems.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(context));
        tvUploadFile.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(context));
        tvContinueshopping.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(context));


        initalizeCartList();

        Switch onOffSwitch = (Switch)  findViewById(R.id.currencySwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);

                if (isChecked){
                    CurrencySymbol=sUSD;
                    selectedCurrency="USD";
                    currencyRateApplied=currencyRateJOD;
                    AppControler.getInstance().put(AppControler.Key.Currency,"USD");
                }
                else {
                    CurrencySymbol=sJOD;
                    selectedCurrency="JOD";
                    currencyRateApplied=currencyRateUSD;
                    AppControler.getInstance().put(AppControler.Key.Currency,"JOD");
                }

                Double value =0.0;
                try {
                    value = Double.parseDouble(TotalGlobal);
                    value=value / currencyRateApplied;
                    if(value.isNaN()){
                        String strV= String.format("%.2f", 0.00);
                        //   String strV= String.valueOf(value);
                        tvtotal.setText(strV + " " + CurrencySymbol);
                        AppControler.getInstance().put(AppControler.Key.TotalPrice,strV);
                    }else{
                        String strV= String.format("%.2f", value);
                        //   String strV= String.valueOf(value);
                        tvtotal.setText(strV + " " + CurrencySymbol);
                        AppControler.getInstance().put(AppControler.Key.TotalPrice,strV);

                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // p did not contain a valid double
                }

                Double svalue =0.0;
                try {

                    svalue = Double.parseDouble(subTotalGlobal);
                    svalue=svalue / currencyRateApplied;
                    if(svalue.isNaN()){
                        tvSubtotal.setText( String.format("%.2f", 0.00) + " " + CurrencySymbol);
                    }else{
                        tvSubtotal.setText( String.format("%.2f", svalue) + " " + CurrencySymbol);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // p did not contain a valid double
                }
                Double txvalue =0.0;
                try {
                    txvalue = Double.parseDouble(TaxGlobal);
                    txvalue=txvalue / currencyRateApplied;
                    if(txvalue.isNaN()){
                        tvTax.setText( String.format("%.2f", 0.00) + " " + CurrencySymbol);
                    }else{
                        tvTax.setText( String.format("%.2f", txvalue) + " " + CurrencySymbol);
                    }


                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // p did not contain a valid double
                }

                tvshipping.setText("0.00 " + CurrencySymbol);
                reloadCartItems();
            }

        });

    }
    private void reloadCartItems() {
        if(adapter != null){
            adapter.updateparsm(selectedCurrency,currencyRateApplied,sJOD,sUSD);
        }

    }
    private void initalizeCartList() {
       cartListingsGlobal=new ArrayList<CartListing>();
     //   cartListings = AppControler.getInstance(this).getCartList(AppConstants.KEY_CART);
        progressDialog.show();
        AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
            @Override
            public void onDataGot(ArrayList<CartListing> cartListings) {
                progressDialog.hide();
                cartListingsGlobal=cartListings;

                tvItems.setText(" " + cartListings.size() + " Item");

                adapter = new CartItemAdapter(context, cartListingsGlobal, onDeleteCartListener,selectedCurrency,currencyRateApplied,sJOD,sUSD);
                recyclerCartItems.setLayoutManager(new LinearLayoutManager(context));
                recyclerCartItems.setAdapter(adapter);

                hitApiForFetchCurrencyRate();
                setTotalPricing();
                hitApiForFetchTokens();

            }

            @Override
            public void onError(String error) {

            }
        });



    }
    private void getCartList() {

        progressDialog.show();
        AppControler.getInstance(context).getCartListGlobal(new cartResponseInterface() {
            @Override
            public void onDataGot(ArrayList<CartListing> cartListings) {
                progressDialog.hide();
                cartListingsGlobal=cartListings;

                if (cartListings == null)
                {
                    // cartListings = new ArrayList<>();
                    relativeLayoutCartMain.setVisibility(View.GONE);
                    linearLayoutEmptyCart.setVisibility(View.VISIBLE);
                }
                else if (cartListings.size() == 1)
                    tvItems.setText(" " + cartListings.size() + " Item");
                else{
                    tvItems.setText(" " + cartListings.size() + " Items");
                }



            }

            @Override
            public void onError(String error) {

            }
        });



    }
    private void hitApiForFetchCurrencyRate() {

        progressDialog.show();

        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<CurrencyRateResponseModel> callGenerateToken = apiService.getCurrencyRate();

        callGenerateToken.enqueue(new Callback<CurrencyRateResponseModel>() {
            @Override
            public void onResponse(Call<CurrencyRateResponseModel> call, Response<CurrencyRateResponseModel> response) {
                if (response.isSuccessful()&&response.body().getCurrencies() != null) {
                    Currencies currencies =  response.body().getCurrencies();
                    Jod jod = currencies.getJod();
                    currencyRateJOD=jod.getRate();
                    Usd usd = currencies.getUsd();
                    currencyRateUSD=usd.getRate();
                   currencyRateApplied=currencyRateUSD;


                } else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CurrencyRateResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void hitApiToRemoveProductFromCart(String cart_id) {

        progressDialog.show();
        String user_id= AppControler.getInstance().getString(AppControler.Key.USER_ID,"");
        String device_Token = ""; // AppControler.getInstance().getString(AppControler.Key.DEVICE_TOKEN, "");
//        if (user_id.isEmpty() && device_Token.isEmpty()) {
//            progressDialog.dismiss();
//            return;
//        }

        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<addcartResponseModel> callGenerateToken = apiService.RemoveProductFromCartAPICall(user_id,device_Token,cart_id);

        callGenerateToken.enqueue(new Callback<addcartResponseModel>() {
            @Override
            public void onResponse(Call<addcartResponseModel> call, Response<addcartResponseModel> response) {
                if (response.isSuccessful()&&response.body().getMessage()!=null) {
                    getCartList();
                } else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<addcartResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void setTotalPricing() {
        double amount = shipping_fee;
        if (cartListingsGlobal.size()>0) {
            currencySymbol = cartListingsGlobal.get(0).getCurrencySymbol();
            for (int i = 0; i < cartListingsGlobal.size(); i++) {
                String strAmount = cartListingsGlobal.get(i).getAmount();
                if (strAmount!= null && !strAmount.equals("") && strAmount.contains(","))
                    strAmount = strAmount.replace(",", "");

                if (strAmount!= null && !strAmount.equals("")) {
                    amount = amount + Double.parseDouble(strAmount);
                }else{
                    amount = 0;
                }
            }
        }
        subtotalAmount = amount;
        setAmountTexts(amount);

    }

    private void setAmountTexts(double amount) {
        DecimalFormat format = new DecimalFormat("#");
        format.setMinimumFractionDigits(2);
        tax = amount * 0.16; //16 % tax
      //  tvtotal.setText(currencySymbol + format.format(amount + tax));
       // tvSubtotal.setText(currencySymbol + format.format(amount));
       // tvTax.setText(currencySymbol+ format.format(tax));

        subTotalGlobal=format.format(amount);
        TotalGlobal=format.format(amount+tax);
        TaxGlobal=format.format(tax);
        if(selectedCurrency=="JOD"){
            Double valueP=Double.parseDouble(TotalGlobal);
            valueP=valueP/currencyRateApplied;
            String strV= String.format("%.2f", valueP);
            tvtotal.setText(sJOD + " " + strV);

            Double valueP1=Double.parseDouble(subTotalGlobal);
            valueP1=valueP1/currencyRateApplied;
            String strV1= String.format("%.2f", valueP1);
            tvSubtotal.setText(sJOD + " " + strV1);

            Double valueP2=Double.parseDouble(TaxGlobal);
            valueP2=valueP2/currencyRateApplied;
            String strV2= String.format("%.2f", valueP2);
            tvTax.setText(sJOD + " " + strV2);
            tvshipping.setText(sJOD + "0" + format.format(shipping_fee));
            AppControler.getInstance().put(AppControler.Key.Currency,"JOD");
            AppControler.getInstance().put(AppControler.Key.TotalPrice,strV);
        }
        else
        {
            tvtotal.setText(sUSD + " " + TotalGlobal);
            tvSubtotal.setText(sUSD + " " + subTotalGlobal);
            tvTax.setText(sUSD + " " + TaxGlobal);
            tvshipping.setText(sUSD + "0" + format.format(shipping_fee));
            AppControler.getInstance().put(AppControler.Key.TotalPrice,TotalGlobal);
            AppControler.getInstance().put(AppControler.Key.Currency,"USD");
        }



    }



    private void checkIfCouponValidate() {
        boolean isCouponFound = false;
        double percentage = 0.0;
        double fixed = 0.0;
        String coupon = editTextCoupon.getText().toString();
        String expiryDate = "";
        for (int i = 0; i < couponsList.size(); i++) {
            if (couponsList.get(i).getCode().equalsIgnoreCase(coupon)) {
                isCouponFound = true;
                selectedCoupon = couponsList.get(i);
                expiryDate = selectedCoupon.getDateExpires();
                AppControler.getInstance(this).put(AppConstants.KEY_COUPON_ID, selectedCoupon.getCode());
                if (selectedCoupon.getDiscountType().equalsIgnoreCase("percent")) {
                    percentage = Double.parseDouble(selectedCoupon.getAmount());
                } else {
                    fixed = Double.parseDouble(selectedCoupon.getAmount());
                }
                break;
            }
        }
        if (isCouponFound) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date strDate = null;
            try {
                strDate = sdf.parse(expiryDate);
                if (new Date().after(strDate)) {
                    Toast.makeText(context, "Incorrect coupon code", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Toast.makeText(context, "Coupon Applied Successfully", Toast.LENGTH_SHORT).show();
            AppControler.getInstance(this).dismissKeyboard(this, editTextCoupon);
            if (fixed > 0.0) {
                setAmountTexts(subtotalAmount - fixed);
            } else if (percentage > 0.0) {
                double amount = percentage * subtotalAmount;
                double finalPercentAmount = amount / 100;
                setAmountTexts(subtotalAmount - finalPercentAmount);
            }
        } else
            Toast.makeText(context, "Incorrect coupon code", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteProduct(int position) {
        CartListing objectTobeRemoved=cartListingsGlobal.get(position);

        cartListingsGlobal.remove(position);
        AppControler.getInstance(this).addProductToCart(cartListingsGlobal, AppConstants.KEY_CART);


        adapter.customNotify(cartListingsGlobal);
        setTotalPricing();
        String user_id= AppControler.getInstance().getString(AppControler.Key.USER_ID,"");
        if (user_id != "")  {
            hitApiToRemoveProductFromCart(objectTobeRemoved.getCart_id());
        }



    }

    private void fetchCouponListing(String consumer_key, String consumer_secret) {
        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<CouponsResponse> callGenerateToken = apiService.getCouponListing(
                consumer_key, consumer_secret, "coupons");

        callGenerateToken.enqueue(new Callback<CouponsResponse>() {
            @Override
            public void onResponse(Call<CouponsResponse> call, Response<CouponsResponse> response) {
                if (response.isSuccessful() && response.body().getData().size() > 0) {
                    couponsList.addAll(response.body().getData());
                } else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CouponsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hitApiForFetchTokens() {

        progressDialog.show();

        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<GenerateTokenResponse> callGenerateToken = apiService.generateToken();

        callGenerateToken.enqueue(new Callback<GenerateTokenResponse>() {
            @Override
            public void onResponse(Call<GenerateTokenResponse> call, Response<GenerateTokenResponse> response) {
                if (response.isSuccessful()) {
                    consumer_key = response.body().getData().getConsumerKey();
                    consumer_secret = response.body().getData().getConsumerSecret();
                    fetchCouponListing(consumer_key, consumer_secret);
                } else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GenerateTokenResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
