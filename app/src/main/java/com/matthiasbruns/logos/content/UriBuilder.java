package com.matthiasbruns.logos.content;

import android.net.Uri;

/**
 * Created by Matthias Bruns on 17/02/15.
 * Used to provide a default interface to create content provider UIRs
 */
public interface UriBuilder<T> {

    public Uri buildInsertUri();

    public Uri buildDeleteUri();

    public Uri buildDeleteUri(long id);

    public Uri buildDeleteUri(T history);

    public Uri buildQueryUri(long id);

    public Uri buildQueryUri(T history);

    public Uri buildUpdateUri(long id);

    public Uri buildUpdateUri(T history);

}
