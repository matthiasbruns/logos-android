package com.matthiasbruns.logos.content;

/**
 * Created by mbruns on 06/12/2016.
 */


import com.matthiasbruns.logos.util.Utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by traxdata on 06/11/15.
 */
public abstract class BaseContentLoader<TYPE, ID, MODEL> implements ContentLoader<TYPE, ID, MODEL> {

    private static final String TAG = BaseContentLoader.class.getSimpleName();

    private final List<PublishCallback<MODEL>> mPublishCallbackList;

    private WeakReference<AppCompatActivity> mAppCompatActivityWeakReference;

    private boolean mDirty;

    private ID mId;

    private int mMode;

    private boolean mPausing;

    private String mSelection;

    private String[] mSelectionArgs;

    private String mSortOrder;

    public BaseContentLoader() {
        mPublishCallbackList = new ArrayList<>();
    }

    protected AppCompatActivity getAppCompatActivity() {
        if (mAppCompatActivityWeakReference != null) {
            return mAppCompatActivityWeakReference.get();
        }
        return null;
    }

    protected abstract void publish(final TYPE t);

    protected abstract void startLoaders();

    protected abstract void stopLoaders();

    @Override
    public void addCallback(final PublishCallback<MODEL> callback) {
        mPublishCallbackList.add(callback);
    }

    @Override
    public void apply() {
        if (mDirty) {
            stopLoaders();
            startLoaders();
            mDirty = false;
        }
    }

    @Override
    public void destroy() {
        mPublishCallbackList.clear();
        if (mAppCompatActivityWeakReference != null) {
            mAppCompatActivityWeakReference.clear();
            mAppCompatActivityWeakReference = null;
        }
    }

    @Nullable
    public ID getId() {
        return mId;
    }

    public int getMode() {
        return mMode;
    }

    public List<PublishCallback<MODEL>> getPublishCallbackList() {
        return mPublishCallbackList;
    }

    @Nullable
    public String getSelection() {
        return mSelection;
    }

    @Nullable
    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    @Nullable
    public String getSortOrder() {
        return mSortOrder;
    }

    public boolean isPausing() {
        return mPausing;
    }

    @Override
    public void removeCallback(final PublishCallback<MODEL> callback) {
        mPublishCallbackList.remove(callback);
    }

    @Override
    public void setId(@NonNull final ID id) {
        mId = id;
    }

    @Override
    public void setMode(final int mode) {
        mMode = mode;
    }

    @Override
    public void setSelection(@Nullable final String selection) {
        mDirty = Utils.equals(mSelection, selection);

        mSelection = selection;
    }

    @Override
    public void setSelectionArgs(@Nullable final String[] selectionArgs) {
        mDirty = Utils.equals(mSelectionArgs, selectionArgs);

        mSelectionArgs = selectionArgs;
    }

    @Override
    public void setSortOrder(@Nullable final String sortOrder) {
        mDirty = Utils.equals(mSortOrder, sortOrder);

        mSortOrder = sortOrder;
    }

    @Override
    public void start(@NonNull final AppCompatActivity activity) {
        if (mAppCompatActivityWeakReference != null) {
            mAppCompatActivityWeakReference.clear();
        }
        mAppCompatActivityWeakReference = new WeakReference<>(activity);
        mPausing = false;
        startLoaders();
        mDirty = false;
    }

    @Override
    public void stop() {
        mPausing = true;
        stopLoaders();
        if (mAppCompatActivityWeakReference != null) {
            mAppCompatActivityWeakReference.clear();
        }
    }
}