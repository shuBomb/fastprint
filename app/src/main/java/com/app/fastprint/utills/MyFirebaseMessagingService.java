package com.app.fastprint.utills;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.app.fastprint.R;
import com.app.fastprint.ui.notification.NotificationActivity;
import com.app.fastprint.ui.splash.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String token;
    String NOTIF_CHANNEL_DEFAULT = "default_010";


    public MyFirebaseMessagingService() {
    }
    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.d("token++++++","token++++++"+token);
                        AppControler.getInstance(getApplicationContext()).put(AppControler.Key.DEVICE_TOKEN,token);
                    }
                });
    }

    public static NotificationManager getNotificationManager(final Context context, String channel) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The user-visible name of the channel.
            CharSequence name = context.getString(R.string.notification_channel_default);
            // The user-visible description of the channel.
            String description = context.getString(R.string.notification_channel_description_default);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channel, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            notificationManager.createNotificationChannel(mChannel);
        }
        return notificationManager;
    }




    @Override
    public void onNewToken(String token) {
        this.token=token;
        AppControler.getInstance(getApplicationContext()).put(AppControler.Key.DEVICE_TOKEN,token);
        Log.d("token++++++","token++++++"+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("NotificationReceived","received"+remoteMessage);
        Log.d("NotificationReceived","notification"+remoteMessage.getNotification());
      //  String click_action = remoteMessage.getNotification().getClickAction();

        showOtherNotification(getApplicationContext(),
                remoteMessage.getData().get("message"));
    }

    private void showOtherNotification(Context context, String body) {


        String title = "FastPrint";
        int notificationId = 10;
        String channelId = "${context.packageName}-FastPrint";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(channelId, title, importance);
            channel.setDescription(body);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentText(body)
                .setColor(getResources().getColor(R.color.colorSkyBlue))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationManager.notify(notificationId, mBuilder.build());
    }
}
