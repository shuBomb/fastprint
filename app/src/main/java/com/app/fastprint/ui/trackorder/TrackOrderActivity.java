package com.app.fastprint.ui.trackorder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fastprint.R;
import com.app.fastprint.baseClass.BaseClass;
import com.app.fastprint.utills.UtilsFontFamily;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrackOrderActivity extends BaseClass {

    ImageView imgBack;
    TextView tvTittle;
    TextView ordernumber;
    TextView tvordernumber;
    TextView arrival;
    TextView tvarrival;
    TextView tvOrderProcessed;
    TextView tvOrderShipped;
    TextView tvOrderRoute;
    TextView tvOrderArrived;
    LinearLayout layoutImage;
    TextView tvCancelOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        ordernumber = findViewById(R.id.ordernumber);
        tvordernumber = findViewById(R.id.tvordernumber);
        arrival = findViewById(R.id.arrival);
        tvarrival = findViewById(R.id.tvarrival);
        tvOrderProcessed = findViewById(R.id.tvOrderProcessed);
        tvOrderShipped = findViewById(R.id.tvOrderShipped);
        tvOrderRoute = findViewById(R.id.tvOrderRoute);
        tvOrderArrived = findViewById(R.id.tvOrderArrived);
        layoutImage = findViewById(R.id.layoutImage);
        tvCancelOrder = findViewById(R.id.tvCancelOrder);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvOrderShipped.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvOrderArrived.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvOrderRoute.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvOrderProcessed.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        ordernumber.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        arrival.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        tvCancelOrder.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));

    }

}