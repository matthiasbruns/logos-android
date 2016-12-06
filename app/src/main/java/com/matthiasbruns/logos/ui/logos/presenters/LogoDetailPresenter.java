package com.matthiasbruns.logos.ui.logos.presenters;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.content.ContentLoader;
import com.matthiasbruns.logos.content.logos.loaders.RxLogoLoader;
import com.matthiasbruns.logos.ui.logos.views.LogoDetailView;

import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.rx.RxTiPresenterSubscriptionHandler;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoDetailPresenter extends TiPresenter<LogoDetailView> {

    private static final String TAG = LogoDetailPresenter.class.getName();

    private RxLogoLoader mLogoLoader;

    private RxTiPresenterSubscriptionHandler rxHelper = new RxTiPresenterSubscriptionHandler(this);

    @Override
    protected void onAttachView(@NonNull final LogoDetailView view) {
        super.onAttachView(view);
        mLogoLoader = RxLogoLoader.Builder().mode(ContentLoader.MODE_FIND_ID).build();
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

        rxHelper.manageSubscription(
                mLogoLoader.observe()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(logos -> logos.get(0))
                        .subscribe(logo -> {
                            getView().onData(logo);
                            getView().setLoading(false);
                        })
        );

        final long logoId = getView().getLogoId();
        if (logoId > 0) {
            mLogoLoader.setId(logoId);
            mLogoLoader.onStart((AppCompatActivity) getView().getActivity());
        } else {
            getView().setLoading(false);
            getView().showErrorRx(
                    getView().getContext().getString(
                            R.string.logo_detail_error_invalid_logo_id))
                    .subscribe(
                            new Action1<Integer>() {
                                @Override
                                public void call(final Integer integer) {
                                    if (LogoDetailView.DIALOG_BUTTON_NEGATIVE == integer) {
                                        getView().getActivity().finish();
                                    }
                                }
                            });
        }
    }
}
