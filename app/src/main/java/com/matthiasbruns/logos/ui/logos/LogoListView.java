package com.matthiasbruns.logos.ui.logos;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mbruns on 30/11/2016.
 */

public interface LogoListView extends TiView {

    @NonNull
    Context getContext();

    @Nullable
    Activity getActivity();

    @DistinctUntilChanged
    @MainThread
    void startLoading();

    @DistinctUntilChanged
    @MainThread
    void stopLoading();

    void setAdapter(@NonNull final RecyclerView.Adapter adapter);
}
