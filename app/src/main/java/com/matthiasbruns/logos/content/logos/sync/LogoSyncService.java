package com.matthiasbruns.logos.content.logos.sync;

import com.matthiasbruns.logos.content.logos.adapters.LogosSyncAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();

    private static LogosSyncAdapter mLogoSyncAdapter;

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return mLogoSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (mLogoSyncAdapter == null) {
                mLogoSyncAdapter = new LogosSyncAdapter(getApplicationContext(), true);
            }
        }
    }
}
