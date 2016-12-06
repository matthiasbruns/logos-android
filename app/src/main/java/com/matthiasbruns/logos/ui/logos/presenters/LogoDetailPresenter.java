package com.matthiasbruns.logos.ui.logos.presenters;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.caverock.androidsvg.SVG;
import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.content.ContentLoader;
import com.matthiasbruns.logos.content.logos.Logo;
import com.matthiasbruns.logos.content.logos.loaders.RxLogoLoader;
import com.matthiasbruns.logos.ui.logos.views.LogoDetailView;
import com.matthiasbruns.logos.util.GlideHelper;

import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.rx.RxTiPresenterSubscriptionHandler;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;

import java.io.InputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoDetailPresenter extends TiPresenter<LogoDetailView> {

    private static final String TAG = LogoDetailPresenter.class.getName();

    private RxLogoLoader mLogoLoader;

    private BehaviorSubject<Logo> mOnLogoLoadedSubject = BehaviorSubject.create();

    private BehaviorSubject<PictureDrawable> mOnSVGLoadedSubject = BehaviorSubject.create();

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    private RxTiPresenterSubscriptionHandler rxHelper = new RxTiPresenterSubscriptionHandler(this);

    @Override
    protected void onAttachView(@NonNull final LogoDetailView view) {
        super.onAttachView(view);
        mLogoLoader = RxLogoLoader.Builder().mode(ContentLoader.MODE_FIND_ID).build();
        mRequestBuilder = new GlideHelper().getSVGLoader(view.getContext());
        initInternals();
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

    private Bitmap asBitmap(PictureDrawable drawable) {
        final Picture picture = drawable.getPicture();
        PictureDrawable pd = new PictureDrawable(picture);
        Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pd.getPicture());
        return bitmap;
    }

    private Observable<Palette> fromBitmap(final Bitmap bitmap) {
        return Observable.create(subscriber -> {
            if (bitmap != null && !bitmap.isRecycled()) {
                Palette.from(bitmap)
                        .generate(subscriber::onNext);
            } else {
                subscriber.onError(new RuntimeException("Bitmap is not set or recycled"));
            }
        });
    }

    private void initInternals() {
        rxHelper.manageSubscription(mOnLogoLoadedSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logo -> {
                    getView().onData(logo);
                    getView().setLoading(false);
                    loadSVG(logo);
                })
        );

        rxHelper.manageSubscription(mOnSVGLoadedSubject
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(drawable -> {
                    getView().setLogoImage(drawable);
                    return drawable;
                })
                .map(this::asBitmap)
                .flatMap(this::fromBitmap)
                .subscribe(palette -> {
                    if (palette != null) {
                        int defaultColor = ContextCompat
                                .getColor(getView().getContext(), R.color.primary);

                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        Palette.Swatch vibrantLightSwatch = palette.getLightVibrantSwatch();
                        Palette.Swatch vibrantDarkSwatch = palette.getDarkVibrantSwatch();
                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                        Palette.Swatch mutedLightSwatch = palette.getLightMutedSwatch();
                        Palette.Swatch mutedDarkSwatch = palette.getDarkMutedSwatch();

                        getView().setToolbarColor(palette.getMutedColor(defaultColor));
                    }
                })
        );
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
                            mOnLogoLoadedSubject.onNext(logo);
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
                    .subscribe(integer -> {
                        if (LogoDetailView.DIALOG_BUTTON_NEGATIVE == integer) {
                            getView().getActivity().finish();
                        }
                    });
        }
    }

    private void loadSVG(final Logo logo) {
        mRequestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                // SVG cannot be serialized so it's not worth to cache it
                .load(Uri.parse(logo.getLogoUrl()))
                .into(new SimpleTarget<PictureDrawable>() {
                    @Override
                    public void onResourceReady(final PictureDrawable resource,
                            final GlideAnimation<? super PictureDrawable> glideAnimation) {
                        mOnSVGLoadedSubject.onNext(resource);
                    }
                });
    }
}
