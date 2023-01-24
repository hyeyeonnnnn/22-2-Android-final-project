package ddwu.mobile.finalproject.ma01_20200554;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "알람!", Toast.LENGTH_LONG).show();

        Intent pIntent = new Intent(context, MainActivity.class);
        pIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, pIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"test")
                .setSmallIcon(R.drawable.mukzzang)
                .setContentTitle(intent.getStringExtra("alarmName"))
                .setContentText(intent.getStringExtra("alarmText"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 100;
        notificationManager.notify(notificationId, builder.build());


    }
}

