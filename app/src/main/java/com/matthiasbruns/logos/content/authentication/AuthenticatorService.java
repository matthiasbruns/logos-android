package com.matthiasbruns.logos.content.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by mbruns on 06/12/2016.
 */

public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private Authenticator mAuthenticator;

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }
}
