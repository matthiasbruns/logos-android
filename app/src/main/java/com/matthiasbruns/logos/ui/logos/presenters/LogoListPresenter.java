package com.matthiasbruns.logos.ui.logos.presenters;

import com.matthiasbruns.logos.content.logos.Logo;
import com.matthiasbruns.logos.content.logos.adapters.LogosAdapter;
import com.matthiasbruns.logos.content.logos.loaders.RxLogoLoader;
import com.matthiasbruns.logos.ui.logos.activities.LogoDetailActivity;
import com.matthiasbruns.logos.ui.logos.views.LogoListView;

import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.rx.RxTiPresenterSubscriptionHandler;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogoListPresenter extends TiPresenter<LogoListView> {

    private static final String TAG = LogoListPresenter.class.getName();

    private RxLogoLoader mLogoLoader;

    private LogosAdapter mLogosAdapter;

    private RxTiPresenterSubscriptionHandler rxHelper = new RxTiPresenterSubscriptionHandler(this);

    @Override
    protected void onAttachView(@NonNull final LogoListView view) {
        super.onAttachView(view);
        mLogoLoader = RxLogoLoader.Builder().build();
        mLogosAdapter = new LogosAdapter(view.getContext());
        view.setAdapter(mLogosAdapter);

        initViewHandlers();
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

    private void initViewHandlers() {
        rxHelper.manageViewSubscription(
                Observable.create((Observable.OnSubscribe<Logo>) subscriber -> {
                    mLogosAdapter.addClickListener((view, position) -> {
                        final Logo logo = mLogosAdapter.getLogos().get(position);
                        final Intent intent = LogoDetailActivity
                                .create(getView().getContext(), logo.getId());
                        getView().getContext().startActivity(intent);
                    });
                }).subscribe()
        );

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
