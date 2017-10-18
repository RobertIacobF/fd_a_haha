package aroundwise.nepi.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import aroundwise.nepi.R;
import aroundwise.nepi.activities.discoverActivity.DiscoverActivity;
import aroundwise.nepi.activities.mainActivity.MainActivity;
import aroundwise.nepi.eventbus.Notificationevent;
import aroundwise.nepi.session.Session;
import de.greenrobot.event.EventBus;

/**
 * Created by mihai on 19/09/16.
 */
public class PushNotificationListener extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "type: " + data.getString("type"));
        Log.d(TAG, "url: " + data.getString("object"));

        if (data.containsKey("points")
                && Integer.valueOf(data.getString("points")) > 0) {
            sendNotification(data, message);
        } else if (data.containsKey("type")
                && data.getString("type") != null
                && data.getString("type").equals("receipt_denied")) {
            sendDeniedReceiptNotificaion();
        } else {
            sendNotification(message);
        }
    }

    private void sendDeniedReceiptNotificaion() {
        Intent intent;
        if (Session.readUserFromSharedPref(getApplicationContext()) != null
                && Session.getUser().getId() != -1) {
            intent = new Intent(this, DiscoverActivity.class);
            intent.putExtra("gotoactivity", true);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.launcher_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.launcher))
                .setContentTitle("A-ha!")
                .setContentText("Bonul a fost respins")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String message) {
        Intent intent;
        if (Session.readUserFromSharedPref(getApplicationContext()) != null
                && Session.getUser().getId() != -1) {
            intent = new Intent(this, DiscoverActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.launcher_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.launcher))
                .setContentTitle("A-ha!")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(Bundle data, String message) {
        Intent intent;
        if (Session.readUserFromSharedPref(getApplicationContext()) != null
                && Session.getUser().getId() != -1) {
            intent = new Intent(this, DiscoverActivity.class);
            intent.putExtra("bundle", data);
            EventBus.getDefault().post(new Notificationevent(data.getString("store_logo"), data.getString("mall_logo"), Integer.valueOf(data.getString("points"))));
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("bundle", data);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.launcher_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.launcher))
                .setContentTitle("A-ha!")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
