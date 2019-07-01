package com.example.basicreminnder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get id & message from intent
        int notificationId=intent.getIntExtra("notificationId",0);
        String message=intent.getStringExtra("ToDo");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("notificationId","notificationId",NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager myNotification=
                    (NotificationManager)context.getSystemService(NotificationManager.class);
            myNotification.createNotificationChannel(channel);
        }

        //when notification is tapped,call Mainpage
        Intent mainIntent=new Intent(context,Mainpage.class);
        PendingIntent contentIntent=PendingIntent.getActivity(context,0,mainIntent,0);

        NotificationManager myNotificationManager=
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Prepare Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("LET DO THE TASK")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL);
        //notify
        myNotificationManager.notify(notificationId,builder.build());



    }
}
