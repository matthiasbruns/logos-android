package com.matthiasbruns.logos.ui.logos.presenters;

import com.matthiasbruns.logos.content.logos.adapters.LogosAdapter;
import com.matthiasbruns.logos.content.logos.loaders.RxLogoLoader;
import com.matthiasbruns.logos.ui.logos.LogoListView;

import net.grandcentrix.thirtyinch.TiPresenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogoListPresenter extends TiPresenter<LogoListView> {

    private static final String TAG = LogoListPresenter.class.getName();

    private RxLogoLoader mLogoLoader;

    private LogosAdapter mLogosAdapter;

    @Override
    protected void onAttachView(@NonNull final LogoListView view) {
        super.onAttachView(view);
        mLogoLoader = RxLogoLoader.Builder().build();
        mLogosAdapter = new LogosAdapter(view.getContext());
        view.setAdapter(mLogosAdapter);
        loadLogos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLogoLoader.onDestroy();
    }

    @Override
    protected void onDetachView() {
        super.onDetachView();
        mLogoLoader.onStop();
    }

    private void loadLogos() {
        if (getView() == null) {
            Log.w(TAG, "View not loaded yet");
            return;
        }

        mLogoLoader.observe().subscribe(logos -> {
            mLogosAdapter.setLogos(logos);
        });

        final Activity activity = getView().getActivity();
        if (activity != null) {
            mLogoLoader.onStart((AppCompatActivity) activity);
        }
    }
}
