package com.matthiasbruns.logos.network;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogoServiceFactory {

    @NonNull
    private WeakReference<Context> mContextWeakReference;

    public LogoServiceFactory(final Context context) {
        mContextWeakReference = new WeakReference<Context>(context);
    }

    @NonNull
    public LogoService create(final String endPoint) {
        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(endPoint)
                .build();

        return retrofit.create(LogoService.class);
    }
}
