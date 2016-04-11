package com.example.dayanidhi.pillion;

/**
 * Created by arvind on 13/02/16.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NotificationService extends AccessibilityService {
    NotificationManager manager;
    Notification myNotication;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        System.out.println("onAccessibilityEvent");
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            String pack=event.getPackageName().toString();
                    System.out.println("PACKAGE" + pack);
            String message=event.getText().toString();
            System.out.println("notification: " + event.getText());
            try
            {
                // Creates a trace file in the primary external storage space of the
                // current application.
                // If the file does not exists, it is created.
                File traceFile = new File((this).getExternalFilesDir(null), "TraceFile.txt");
                if (!traceFile.exists())
                    traceFile.createNewFile();
                // Adds a line to the trace file
                BufferedWriter writer = new BufferedWriter(new FileWriter(traceFile, true /*append*/));
                writer.write(message);
                writer.close();
                // Refresh the data so it can seen when the device is plugged in a
                // computer. You may have to unplug and replug the device to see the
                // latest changes. This is not necessary if the user should not modify
                // the files.
                MediaScannerConnection.scanFile((this),
                        new String[]{traceFile.toString()},
                        null,
                        null);

            }
            catch (IOException e)
            {
                Log.e("Error", "Not able to create file...");
            }




            if(message.contains("drive")||message.contains("driving")||message.contains("come")||message.contains("meet")||message.contains("see you at")) {
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


                Intent intent1 = new Intent(this, LoginActivity.class);
                intent1.putExtra("dp", "");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent1, 0);

                Notification.Builder builder = new Notification.Builder(this);

                builder.setAutoCancel(false);
               // builder.setTicker("this is ticker text");
                builder.setContentTitle("Pillion Notification");
                builder.setContentText("It seems you are going out.. Do you want to use Pillion ?");
                builder.setSmallIcon(R.drawable.login);
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(true);
                //builder.setSubText("This is subtext...");   //API level 16
                builder.setNumber(100);
                builder.build();

                myNotication = builder.getNotification();
                manager.notify(5, myNotication);


            }
        }
    }
    @Override
    protected void onServiceConnected() {
        System.out.println("onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        System.out.println("onInterrupt");
    }
}