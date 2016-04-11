package com.example.dayanidhi.pillion;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by arvind on 09/01/16.
 */
public  class IncomingSms extends BroadcastReceiver {
    NotificationManager manager;
    Notification myNotication;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
public AccessibilityService as;
    public void onReceive(Context context, Intent intent) {





        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: " + senderNum + ", message: " + message,Toast.LENGTH_SHORT);




                    String msg = message.substring(message.indexOf(":")+1);
                    Toast.makeText(context,"OTP:"+msg,Toast.LENGTH_SHORT).show();




                   // toast.show();
                    OTP.progressDialog.dismiss();
                    OTP.et.setText(msg);
                   // OTP a = new OTP();
                  //  a.setOTP(msg);
                    // et.setText(msg);



                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
