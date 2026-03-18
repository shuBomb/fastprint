package com.app.fastprint.ui.notification;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fastprint.R;
import com.app.fastprint.services.APIClient;
import com.app.fastprint.services.APIInterface;
import com.app.fastprint.ui.notification.adapters.NotificationAdapter;
import com.app.fastprint.ui.notification.notificationResponse.DataItem;
import com.app.fastprint.ui.notification.notificationResponse.NotificationResponse;
import com.app.fastprint.utills.UtilsAlertDialog;
import com.app.fastprint.utills.UtilsFontFamily;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ImageView imgBack;
    TextView tvTittle;
    RecyclerView recyclerNotification;
    Context context;
    NotificationAdapter notificationAdapter;
    private Dialog progressDialog;
    private ArrayList<DataItem> dataItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        imgBack = findViewById(R.id.imgBack);
        tvTittle = findViewById(R.id.tvTittle);
        recyclerNotification = findViewById(R.id.recyclerNotification);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        context = NotificationActivity.this;
        progressDialog = UtilsAlertDialog.progressDialog(context);
        tvTittle.setTypeface(UtilsFontFamily.typeFaceForRobotoMedium(this));
        notificationAdapter = new NotificationAdapter(context,dataItems);
        recyclerNotification.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotification.setAdapter(notificationAdapter);

        hitApiForFetchNotifications();
    }




    private void hitApiForFetchNotifications() {

        progressDialog.show();

        APIInterface apiService = APIClient.getRetrofitInstance().create(APIInterface.class);
        Call<NotificationResponse> callGenerateToken = apiService.getNotifications();

        callGenerateToken.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful()&&response.body().getData().size()>0) {
                    dataItems.addAll(response.body().getData());
                    notificationAdapter.customNotify(dataItems);
                } else
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
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