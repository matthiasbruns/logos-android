package com.matthiasbruns.logos.content.logos.loaders;

import com.matthiasbruns.logos.content.ContentLoader;
import com.matthiasbruns.logos.content.logos.Logo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by mbruns on 06/12/2016.
 */

public class RxLogoLoader {

    public static class Builder {

        private static final String TAG = Builder.class.getSimpleName();

        private RxLogoLoader mRxLogoLoader;

        private Builder() {
            mRxLogoLoader = new RxLogoLoader();
            mode(ContentLoader.MODE_QUERY);
        }

        public RxLogoLoader build() {
            return mRxLogoLoader;
        }

        public Builder id(long id) {
            mRxLogoLoader.setId(id);
            return this;
        }

        public Builder mode(int mode) {
            mRxLogoLoader.setMode(mode);
            return this;
        }

        public Builder selection(@Nullable String selection) {
            mRxLogoLoader.setSelection(selection);
            return this;
        }

        public Builder selectionArgs(@Nullable final String[] selectionArgs) {
            mRxLogoLoader.setSelectionArgs(selectionArgs);
            return this;
        }

        public Builder sort(@Nullable String sortOrder) {
            mRxLogoLoader.setSortOrder(sortOrder);
            return this;
        }

    }

    private static final String TAG = RxLogoLoader.class.getSimpleName();

    private LogoLoader mLogoLoader;

    private RxLogoLoader() {
        mLogoLoader = new LogoLoader();
    }

    public static Builder Builder() {
        return new Builder();
    }

    public void apply() {
        mLogoLoader.apply();
    }

    public Observable<List<Logo>> observe() {
        return Observable.create(new Observable.OnSubscribe<List<Logo>>() {
            @Override
            public void call(final Subscriber<? super List<Logo>> subscriber) {
                mLogoLoader.addCallback(new ContentLoader.PublishCallback<List<Logo>>() {
                    @Override
                    public void onPublish(final List<Logo> locations) {
                        subscriber.onNext(locations);
                    }
                });
            }
        });
    }

    public void onDestroy() {
        mLogoLoader.destroy();
    }

    public void onStart(@NonNull final AppCompatActivity activity) {
        mLogoLoader.start(activity);
    }

    public void onStop() {
        mLogoLoader.stop();
    }

    public void setId(@NonNull final Long aLong) {
        mLogoLoader.setId(aLong);
    }

    public void setMode(final int mode) {
        mLogoLoader.setMode(mode);
    }

    public void setSelection(@Nullable final String selection) {
        mLogoLoader.setSelection(selection);
    }

    public void setSelectionArgs(@Nullable final String[] selectionArgs) {
        mLogoLoader.setSelectionArgs(selectionArgs);
    }

    public void setSortOrder(@Nullable final String sortOrder) {
        mLogoLoader.setSortOrder(sortOrder);
    }
}