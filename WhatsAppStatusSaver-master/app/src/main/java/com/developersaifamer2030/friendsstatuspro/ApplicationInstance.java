package com.developersaifamer2030.friendsstatuspro;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

public class ApplicationInstance extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAnalytics.getInstance(getApplicationContext());
    }
}
