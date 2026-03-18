package com.app.fastprint.ui.thankyou;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.services.UpdateStatus;
import com.app.fastprint.ui.cart.responseModel.CartListing;
import com.app.fastprint.ui.main.MainActivity;
import com.app.fastprint.ui.product.responseModel.addcartResponseModel;
import com.app.fastprint.utills.AppConstants;
import com.app.fastprint.utills.AppControler;
import com.app.fastprint.utills.UtilsFontFamily;
import com.google.android.gms.tasks.Task;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThankyouActivity extends BaseClass {

    TextView tvThankyou;
    TextView tvsendEmail;
    TextView tvOrderId;
    TextView tvBackToShop;
    String orderId = "";
    String consumerSecret = "";
    String consumerKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        tvThankyou = findViewById(R.id.tvThankyou);
        tvsendEmail = findViewById(R.id.tvsendEmail);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvBackToShop = findViewById(R.id.tvBackToShop);
        tvBackToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThankyouActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        orderId = getIntent().getStringExtra("orderId");
        consumerKey = getIntent().getStringExtra("consumerKey");
        consumerSecret = getIntent().getStringExtra("consumerSecret");
        tvThankyou.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(this));
        tvOrderId.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvBackToShop.setTypeface(UtilsFontFamily.typeFaceForRobotoBold(this));
        AppControler.getInstance(ThankyouActivity.this).addProductToCart(new ArrayList<>(), AppConstants.KEY_CART);

        callApiForUpdateStatus();
        tvOrderId.setText(orderId);

        //if (!AppControler.getInstance(ThankyouActivity.this).getBoolean(AppConstants.KEY_RATING))

        hitApiToRemoveAllProductFromCart();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ThankyouActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void hitApiToRemoveAllProductFromCart() {
        ArrayList<CartListing> list = new ArrayList<CartListing>();
        AppControler.getInstance().addProductToCart(list,AppConstants.KEY_CART);

        String user_id= AppControler.getInstance().getString(AppControler.Key.USER_ID,"");
        String device_Token = "";// AppControler.getInstance().getString(AppControler.Key.DEVICE_TOKEN, "");


        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<addcartResponseModel> callGenerateToken = apiService.RemoveAllProductFromCartAPICall(user_id,device_Token);

        callGenerateToken.enqueue(new Callback<addcartResponseModel>() {
            @Override
            public void onResponse(Call<addcartResponseModel> call, Response<addcartResponseModel> response) {
                if (response.isSuccessful()&&response.body().getMessage()!=null) {

                } else
                    Toast.makeText(ThankyouActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<addcartResponseModel> call, Throwable t) {

                Toast.makeText(ThankyouActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callApiForUpdateStatus() {

        UpdateStatus updateStatus = new UpdateStatus();
        updateStatus.setStatus("completed");
        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<JsonObject> callGenerateToken = apiService.updatePaymentStatus(orderId,
                consumerKey,consumerSecret,updateStatus);

        callGenerateToken.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ThankyouActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
