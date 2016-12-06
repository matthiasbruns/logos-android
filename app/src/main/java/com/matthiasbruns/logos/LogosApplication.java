package com.matthiasbruns.logos;

import com.matthiasbruns.logos.content.config.Config;
import com.matthiasbruns.logos.content.logos.LogoContract;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogosApplication extends Application {

    private static final String TAG = LogosApplication.class.getSimpleName();

    private static Account sAccount;

    private Config mConfig;

    public static Account getSyncAccount() {
        return sAccount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        sAccount = createSyncAccount(getApplicationContext());

        /*mConfig = new Config(getApplicationContext());
        if (mConfig.isFirstLaunch()) {
            mConfig.setFirstLaunch(false);

        }*/
    }

    private Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account syncAccount = new Account(getString(R.string.app_name),
                getString(R.string.account_type));
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        accountManager.addAccountExplicitly(syncAccount, null, null);

        ContentResolver.setIsSyncable(syncAccount, LogoContract.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(syncAccount, LogoContract.AUTHORITY, true);
        ContentResolver
                .addPeriodicSync(syncAccount, LogoContract.AUTHORITY, Bundle.EMPTY, 60 * 60 * 24);
        ContentResolver.requestSync(syncAccount, LogoContract.AUTHORITY, Bundle.EMPTY);

        return syncAccount;
    }
}
