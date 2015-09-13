package com.fiuba.mascota;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Juan Manuel Romera on 13/9/2015.
 */
public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "EcXAT5pXyPP4x874uRSPKgC6cMgd3BBNdK6UKKAq", "cJ8rMk6fCUSx9uTNbz1F915yKaJawtN3VQSTbopX");

        //Facebook Connect
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
    }
}

