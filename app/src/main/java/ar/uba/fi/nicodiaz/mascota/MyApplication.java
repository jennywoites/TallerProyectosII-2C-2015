package ar.uba.fi.nicodiaz.mascota;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.User;

/**
 * Created by Juan Manuel Romera on 13/9/2015.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // Register your parse models
        ParseObject.registerSubclass(Address.class);
        ParseObject.registerSubclass(AdoptionPet.class);
        ParseObject.registerSubclass(MissingPet.class);

        //Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "EcXAT5pXyPP4x874uRSPKgC6cMgd3BBNdK6UKKAq", "cJ8rMk6fCUSx9uTNbz1F915yKaJawtN3VQSTbopX");

        //Facebook Connect
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(this);
    }

    public static Context getContext() {
        return mContext;
    }

}

