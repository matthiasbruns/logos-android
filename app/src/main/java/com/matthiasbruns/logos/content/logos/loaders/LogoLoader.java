package com.matthiasbruns.logos.content.logos.loaders;


import com.matthiasbruns.logos.content.BaseContentLoader;
import com.matthiasbruns.logos.content.logos.Logo;
import com.matthiasbruns.logos.content.logos.LogoContract;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoLoader extends BaseContentLoader<Cursor, Long, List<Logo>> {

    private static final String TAG = LogoLoader.class.getSimpleName();

    private static final int LOADER_LOGOS = 1;

    private static final int LOADER_LOGO_ID = 3;

    @Override
    protected void publish(final Cursor t) {
        final List<Logo> logos = parseCursor(t);

        final List<PublishCallback<List<Logo>>> publishCallbackList = getPublishCallbackList();
        for (PublishCallback<List<Logo>> callback : publishCallbackList) {
            callback.onPublish(logos);
        }
    }

    @Override
    protected void startLoaders() {
        final AppCompatActivity activity = getAppCompatActivity();
        if (!isPausing() && activity != null) {
            final int mode = getMode();
            if (mode == MODE_QUERY || mode == MODE_BOTH) {
                activity.getSupportLoaderManager()
                        .initLoader(LOADER_LOGOS + this.hashCode(), null,
                                this);
            }

            if (mode == MODE_FIND_ID || mode == MODE_BOTH) {
                activity.getSupportLoaderManager()
                        .initLoader(LOADER_LOGO_ID + this.hashCode(), null,
                                this);
            }
        }
    }

    @Override
    protected void stopLoaders() {
        final AppCompatActivity appCompatActivity = getAppCompatActivity();
        if (appCompatActivity != null) {
            // destroy both loaders, even if we only used one
            appCompatActivity.getSupportLoaderManager()
                    .destroyLoader(LOADER_LOGOS + this.hashCode());
            appCompatActivity.getSupportLoaderManager()
                    .destroyLoader(LOADER_LOGO_ID + this.hashCode());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        final AppCompatActivity activity = getAppCompatActivity();
        if (!isPausing() && activity != null) {
            final int code = id - this.hashCode();
            final String[] projection = LogoContract.Logo.Projection.SELECT_ALL;

            switch (code) {
                case LOADER_LOGO_ID: {
                    final Uri contentUri = LogoContract.Logo.UriBuilder.getInstance()
                            .buildQueryUri(getId());
                    return new CursorLoader(activity, contentUri, projection, getSelection(),
                            getSelectionArgs(), getSortOrder());
                }
                case LOADER_LOGOS: {
                    final Uri contentUri = LogoContract.Logo.CONTENT_URI;
                    return new CursorLoader(activity, contentUri, projection, getSelection(),
                            getSelectionArgs(), getSortOrder());
                }
                default:
                    throw new IllegalArgumentException("Loader with id: " + id + " is unknown");
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        if (!isPausing()) {
            switch (loader.getId() - this.hashCode()) {
                case LOADER_LOGO_ID:
                case LOADER_LOGOS:
                    publish(data);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Loader with id: " + loader.getId() + " is unknown");
            }
        }
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        // Some space for dancing kirby <(*. *<) <(* . *)> (>* .*)>
    }

    private List<Logo> parseCursor(final Cursor cursor) {
        final List<Logo> Logos = new ArrayList<>();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Logos.add(new Logo(cursor));
            }
        }
        return Logos;
    }
}