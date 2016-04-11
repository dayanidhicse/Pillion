package com.example.dayanidhi.pillion;

import com.firebase.client.Firebase;

/**
 * Created by Ashu on 20/11/15.
 */
public class ChatApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}