package com.matthiasbruns.logos.ui.logos.views;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;
import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by mbruns on 30/11/2016.
 */

public interface LogoListView extends TiView {

    String TAG = LogoListView.class.getSimpleName();

    String EXTRA_SCROLL_POSITION = TAG + "_scroll_position";

    String EXTRA_SAVED_LAYOUT_MANAGER = TAG + "_saved_layout_manager";

    @Nullable
    Activity getActivity();

    @NonNull
    Context getContext();

    int getRetainedScrollPosition();

    @CallOnMainThread
    void restoreLayoutManagerState();

    @DistinctUntilChanged
    @CallOnMainThread
    void scrollToPosition(int retainedScrollPosition);

    void setAdapter(@NonNull final RecyclerView.Adapter<?> adapter);

    @DistinctUntilChanged
    @CallOnMainThread
    void startLoading();

    @DistinctUntilChanged
    @CallOnMainThread
    void stopLoading();
}
