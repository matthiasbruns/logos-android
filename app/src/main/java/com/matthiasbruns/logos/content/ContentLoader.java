package com.matthiasbruns.logos.content;

/**
 * Created by traxdata on 06/11/15.
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Matthias Bruns on 26/08/15. This interface describes the API for configurable
 * standalone ContentLoaders.
 */
public interface ContentLoader<TYPE, ID, MODEL> extends LoaderManager.LoaderCallbacks<TYPE> {

    interface PublishCallback<MODEL> {

        void onPublish(MODEL t);
    }

    int MODE_FIND_ID = 0;
    int MODE_QUERY = 1;
    int MODE_BOTH = 2;

    void addCallback(final PublishCallback<MODEL> callback);

    /**
     * Apply the changes you have set. Methods: <ul> <li>{@link #setId(Object)}</li> <li>{@link
     * #setMode(int)}</li> <li>{@link #setSelection(String)}</li> <li>{@link
     * #setSelectionArgs(String[])}</li> <li>{@link #setSortOrder(String)}</li> </ul>
     *
     * The current loaders will be reset!
     */
    void apply();

    /**
     * Kills references and destroyed this loader.
     */
    void destroy();

    void removeCallback(final PublishCallback<MODEL> callback);

    /**
     * Set the ID of the object you wand to load.
     *
     * @param id primary key value
     */
    void setId(@NonNull final ID id);

    /**
     * Sets the mode of this loader. Modes: <ul> <li>{@link #MODE_FIND_ID} will search the
     * CONTENT_URI with the ID URI</li> <li>{@link #MODE_QUERY} will search the CONTENT_URI without
     * using the set ID</li> <li>{@link #MODE_BOTH} will search BOTH - uses two parallel
     * loaders</li> </ul>
     */
    void setMode(final int mode);

    /**
     * WHERE clause for this loader
     */
    void setSelection(@Nullable final String selection);

    /**
     * If you use {@link #setSelection(String)} with placeholders, you need to provide the values by
     * using this method.
     */
    void setSelectionArgs(@Nullable final String[] selectionArgs);

    /**
     * Define the sort order of the searched cursor rows
     */
    void setSortOrder(@Nullable final String sortOrder);

    /**
     * Hook this mothod up to the activity lifecycle
     */
    void start(AppCompatActivity activity);

    /**
     * Hook this mothod up to the activity lifecycle
     */
    void stop();

}