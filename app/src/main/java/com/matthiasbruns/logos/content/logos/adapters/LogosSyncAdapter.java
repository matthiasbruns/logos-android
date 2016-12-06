package com.matthiasbruns.logos.content.logos.adapters;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.content.logos.LogoContract;
import com.matthiasbruns.logos.content.logos.sync.LogoSyncService;
import com.matthiasbruns.logos.network.LogoService;
import com.matthiasbruns.logos.network.LogoServiceFactory;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogosSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = LogoSyncService.class.getName();

    // Global variables
    // Define a variable to contain a content resolver instance
    private ContentResolver mContentResolver;

    private LogoService mLogoService;

    public LogosSyncAdapter(final Context context, final boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mLogoService = new LogoServiceFactory(getContext())
                .create(getContext().getString(R.string.service_logo_endpoint));
    }

    public LogosSyncAdapter(final Context context, final boolean autoInitialize,
            final boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mLogoService = new LogoServiceFactory(getContext())
                .create(getContext().getString(R.string.service_logo_endpoint));
    }

    @Override
    public void onPerformSync(final Account account, final Bundle bundle, final String s,
            final ContentProviderClient contentProviderClient, final SyncResult syncResult) {
        Log.d(TAG, "onPerformSync");

        mLogoService
                .get()
                .map(logos -> {
                    // Delete all
                    mContentResolver
                            .delete(LogoContract.Logo.UriBuilder.getInstance().buildDeleteUri(),
                                    null, null);
                    return logos;
                })
                .flatMapIterable(logos -> logos)
                .map(logo -> {
                    mContentResolver.insert(LogoContract.Logo.CONTENT_URI, logo.getContentValues());
                    return logo;
                })
                .toList()
                .subscribe(logos -> {
                    Log.d(TAG, "Logos loaded: " + logos.size());
                });
    }
}
