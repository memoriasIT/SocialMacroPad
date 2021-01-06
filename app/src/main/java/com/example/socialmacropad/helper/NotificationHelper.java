package com.example.socialmacropad.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import com.example.socialmacropad.R;

@Deprecated
// Not used yet, used to generate a notification when service is active
public class NotificationHelper extends ContextWrapper {

    private NotificationManager notificationManager;

    public static final String CHANNEL_SERVICE_ID               = "APPLICATION_SERVICE";
    public static final String CHANNEL_SERVICE_NAME             = "Service";
    public static final String CHANNEL_SERVICE_ABOUT            = "Service notification when app is working in foreground.";

    public NotificationHelper(Context base) {
        super(base);
    }

    public void createChannels(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannelThree = new NotificationChannel(CHANNEL_SERVICE_ID, CHANNEL_SERVICE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannelThree.setDescription(CHANNEL_SERVICE_ABOUT);
            mChannelThree.enableLights(false);
            mChannelThree.enableVibration(false);
            mChannelThree.setLightColor(getColor(R.color.colorPrimary));
            mChannelThree.setShowBadge(true);
            mChannelThree.setSound(null, null);
            getNotificationManager().createNotificationChannel(mChannelThree);
        }
    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }
}