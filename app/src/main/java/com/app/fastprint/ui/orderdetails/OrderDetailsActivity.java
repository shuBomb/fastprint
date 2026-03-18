package com.app.fastprint.ui.orderdetails;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.ui.orderdetails.adapter.MyOrderDetailAdapter;
import com.app.fastprint.ui.orderdetails.orderDetailResponse.LineItemsItem;
import com.app.fastprint.ui.orderdetails.orderDetailResponse.OrderDetailResponse;
import com.app.fastprint.ui.trackorder.TrackOrderActivity;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends BaseClass {

    ImageView imgBack;
    TextView tvTittle;
    ImageView imgProduct;
    TextView tvProductName;
    TextView tvDiscountPrice;
    TextView tvPrice;
    TextView tvorderID;
    TextView tvorderDate;
    TextView tvTrackOrder;
    TextView tvOrderStatus;
    TextView tvDescriptions;
    TextView quantity;
    TextView tvQuantity;
    TextView coveroptions;
    TextView tvCoveroptions;
    TextView pageincluding;
    TextView tvPageincluding;
    TextView tvOrderSummery;
    TextView bagTotal;
    TextView bagDiscount;
    TextView subtotal;
    TextView estimate;
    TextView copunDiscount;
    TextView dChargres;
    TextView totalPayable;
    TextView tvbagTotal;
    TextView tvbagDiscount;
    TextView tvsubtotal;
    TextView tvVatCst;
    TextView tvCoupunDiscount;
    TextView tvDCharges;
    TextView tvTotalPay;
    TextView shippingAddress;
    TextView tvName;
    TextView tvEmail;
    TextView tvPhone;
    TextView tvshippingaddress;
    TextView billingAddress;
    TextView tvBName;
    TextView tvBEmail;
    TextView tvBPhone;
    TextView tvbillingaddress;
    TextView tvEmailInvoice;
    TextView tvNeedHelp;
    RecyclerView recyclerViewOrders;

    private Dialog progressDialog;
    private List<LineItemsItem> ordersResponses;
    private OrderDetailResponse orderDetailResponse;
    private MyOrderDetailAdapter myOrderDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        imgProduct = findViewById(R.id.imgProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvDiscountPrice = findViewById(R.id.tvDiscountPrice);
        tvPrice = findViewById(R.id.tvPrice);
        tvorderID = findViewById(R.id.tvorderID);
        tvorderDate = findViewById(R.id.tvorderDate);
        tvTrackOrder = findViewById(R.id.tvTrackOrder);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvDescriptions = findViewById(R.id.tvDescriptions);
        quantity = findViewById(R.id.quantity);
        tvQuantity = findViewById(R.id.tvQuantity);
        coveroptions = findViewById(R.id.coveroptions);
        tvCoveroptions = findViewById(R.id.tvCoveroptions);
        pageincluding = findViewById(R.id.pageincluding);
        tvPageincluding = findViewById(R.id.tvPageincluding);
        tvOrderSummery = findViewById(R.id.tvOrderSummery);
        bagTotal = findViewById(R.id.bagTotal);
        bagDiscount = findViewById(R.id.bagDiscount);
        subtotal = findViewById(R.id.subtotal);
        estimate = findViewById(R.id.estimate);
        copunDiscount = findViewById(R.id.copunDiscount);
        dChargres = findViewById(R.id.dChargres);
        totalPayable = findViewById(R.id.totalPayable);
        tvbagTotal = findViewById(R.id.tvbagTotal);
        tvbagDiscount = findViewById(R.id.tvbagDiscount);
        tvsubtotal = findViewById(R.id.tvsubtotal);
        tvVatCst = findViewById(R.id.tvVatCst);
        tvCoupunDiscount = findViewById(R.id.tvCoupunDiscount);
        tvDCharges = findViewById(R.id.tvDCharges);
        tvTotalPay = findViewById(R.id.tvTotalPay);
        shippingAddress = findViewById(R.id.shippingAddress);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvshippingaddress = findViewById(R.id.tvshippingaddress);
        billingAddress = findViewById(R.id.billingAddress);
        tvBName = findViewById(R.id.tvBName);
        tvBEmail = findViewById(R.id.tvBEmail);
        tvBPhone = findViewById(R.id.tvBPhone);
        tvbillingaddress = findViewById(R.id.tvbillingaddress);
        tvEmailInvoice = findViewById(R.id.tvEmailInvoice);
        tvNeedHelp = findViewById(R.id.tvNeedHelp);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

        progressDialog = UtilsAlertDialog.progressDialog(this);

        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvProductName.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvPrice.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvorderDate.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvTrackOrder.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        quantity.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        coveroptions.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        pageincluding.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvEmailInvoice.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvNeedHelp.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvorderID.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvOrderStatus.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvOrderSummery.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        shippingAddress.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        billingAddress.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvDiscountPrice.setPaintFlags(tvDiscountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvDiscountPrice.setText("$" + "120.00");

        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        apiOrderDetails(getIntent().getStringExtra("consumer_key"),
                getIntent().getStringExtra("consumer_secret"));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, TrackOrderActivity.class);
                startActivity(intent);
            }
        });
    }



    public void apiOrderDetails(String consumerKey, String consumerSecret) {
        progressDialog.show();

        String orderId =String.valueOf(getIntent().getIntExtra("orderid",0));
        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<OrderDetailResponse> callOrder = apiService.orderDetail(orderId,consumerKey,consumerSecret,"get_order");

        callOrder.enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                progressDialog.dismiss();
                ordersResponses=new ArrayList<>();
                if (response.isSuccessful()) {
                    orderDetailResponse= response.body();
                    ordersResponses.addAll(response.body().getData().getLineItems());
                    setAdapter();
                } else {
                    Toast.makeText(OrderDetailsActivity.this,  "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OrderDetailsActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setAdapter() {
        myOrderDetailAdapter = new MyOrderDetailAdapter(this,ordersResponses,orderDetailResponse.getData().getCurrencySymbol());
        recyclerViewOrders.setAdapter(myOrderDetailAdapter);
    }

}