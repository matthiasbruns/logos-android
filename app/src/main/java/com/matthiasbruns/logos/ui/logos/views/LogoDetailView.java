package com.matthiasbruns.logos.ui.logos.views;

import com.matthiasbruns.logos.content.logos.Logo;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Observable;

/**
 * Created by mbruns on 06/12/2016.
 */

public interface LogoDetailView extends TiView {

    String TAG = LogoDetailView.class.getSimpleName();

    int DIALOG_BUTTON_POSITIVE = 0;

    int DIALOG_BUTTON_NEGATIVE = 1;

    String EXTRA_LOGO_ID = TAG + "_logo_id";

    @DistinctUntilChanged
    @CallOnMainThread
    void applyLogoColor(int rgb);

    @NonNull
    Activity getActivity();

    @NonNull
    Context getContext();

    @IntRange(from = 0, to = Long.MAX_VALUE)
    long getLogoId();

    @DistinctUntilChanged
    @CallOnMainThread
    void onData(@Nullable final Logo logo);

    @NonNull
    Observable<Void> onLogoClicked();

    @CallOnMainThread
    void rotateLogo();

    void setLoading(final boolean loading);

    @DistinctUntilChanged
    @CallOnMainThread
    void setLogoImage(@NonNull Drawable drawable);

    void setShareIntent(Intent intent);

    @DistinctUntilChanged
    @CallOnMainThread
    void showError(final String error);

    @NonNull
    Observable<Integer> showErrorRx(final String error);
}
