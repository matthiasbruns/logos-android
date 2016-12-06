package com.matthiasbruns.logos.content.logos.providers;

import com.matthiasbruns.logos.content.logos.LogoContract;
import com.matthiasbruns.logos.content.logos.LogoDBHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoContentProvider extends ContentProvider {

    private static final String TAG = LogoContentProvider.class.getCanonicalName();

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Will be used by the URIMatcher to tell, which uri matched
    private static final int LOGOS = 1;

    private static final int LOGO_ID = 0;

    private LogoDBHelper mDbHelper;

    @Override
    public int delete(final Uri uri, String selection, final String[] selectionArgs) {
        String table;

        final int match = sURIMatcher.match(uri);
        switch (match) {
            case LOGO_ID:
                String where = new StringBuilder()
                        .append(LogoContract.Logo.Columns._ID)
                        .append("=")
                        .append(uri.getLastPathSegment())
                        .toString();
                if (TextUtils.isEmpty(selection)) {
                    selection = where;
                } else {
                    selection = new StringBuilder(selection)
                            .append(" AND ")
                            .append(where)
                            .toString();
                }
                // no break
            case LOGOS:
                table = LogoContract.Logo.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("delete is not supported for uri: " + uri);
        }

        final int rows = mDbHelper.getWritableDatabase().delete(table, selection, selectionArgs);
        // don't force a UI refresh if nothing has changed
        if (rows > 0 && getContext() != null) {
            getContext().getContentResolver()
                    .notifyChange(LogoContract.Logo.CONTENT_URI, null);
        }
        return rows;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case LOGOS:
                return LogoContract.Logo.TYPE;
            case LOGO_ID:
                return LogoContract.Logo.ITEM_TYPE;
            default:
                throw new IllegalArgumentException("uri: " + uri + " is unknown");
        }
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {

        values.remove(LogoContract.Logo.Columns._ID);

        Uri contentUri;
        String table;

        final int match = sURIMatcher.match(uri);
        switch (match) {
            case LOGOS:
                table = LogoContract.Logo.TABLE_NAME;
                contentUri = LogoContract.Logo.CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("insert is not supported for uri: " + uri);
        }

        final long row = mDbHelper.getWritableDatabase().insert(table, null, values);
        if (row <= 0) {
            Log.w(TAG, "couldn't insert data. uri: " + uri);
            return null;
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(LogoContract.Logo.CONTENT_URI, null);
        }
        return ContentUris.withAppendedId(contentUri, row);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new LogoDBHelper(getContext());
        return true;
    }

    @Override
    public synchronized Cursor query(final Uri uri, String[] projection, String selection,
            final String[] selectionArgs, final String sortOrder) {
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        Cursor cursor = null;
        String groupBy = null;
        String having = null;

        final int match = sURIMatcher.match(uri);
        switch (match) {

            case LOGO_ID: {
                String where = new StringBuilder()
                        .append(LogoContract.Logo.Columns._ID)
                        .append("=")
                        .append(uri.getLastPathSegment())
                        .toString();
                builder.appendWhere(where);
            }
            case LOGOS:
                builder.setTables(LogoContract.Logo.TABLE_NAME);

                break;
            default:
                throw new IllegalArgumentException("uri: " + uri + " is unknown");
        }

        cursor = builder
                .query(mDbHelper.getReadableDatabase(), projection, selection, selectionArgs,
                        groupBy,
                        having, sortOrder);

        if (cursor == null) {
            Log.w(TAG, "query failed. Uri: " + builder
                    .buildQuery(projection, selection, selectionArgs, groupBy, having, sortOrder,
                            ""));
        } else if (getContext() != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),
                    LogoContract.Logo.CONTENT_URI);
        }

        return cursor;
    }

    @Override
    public int update(final Uri uri, final ContentValues values, String selection,
            final String[] selectionArgs) {
        String table;

        final int match = sURIMatcher.match(uri);
        switch (match) {
            case LOGO_ID:
                String where = new StringBuilder()
                        .append(LogoContract.Logo.Columns._ID)
                        .append("=")
                        .append(uri.getLastPathSegment())
                        .toString();
                if (TextUtils.isEmpty(selection)) {
                    selection = where;
                } else {
                    selection = new StringBuilder(selection)
                            .append(" AND ")
                            .append(where)
                            .toString();
                }
                // no break
            case LOGOS:
                table = LogoContract.Logo.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("update is not supported for uri: " + uri);
        }

        final int rows = mDbHelper.getWritableDatabase().update(table, values, selection,
                selectionArgs);
        // don't force a UI refresh if nothing has changed
        if (rows > 0 && getContext() != null) {
            getContext().getContentResolver()
                    .notifyChange(LogoContract.Logo.CONTENT_URI, null);
        }

        return rows;
    }

    {
        sURIMatcher.addURI(LogoContract.AUTHORITY, LogoContract.Logo.BASE_PATH, LOGOS);
        sURIMatcher.addURI(LogoContract.AUTHORITY, LogoContract.Logo.BASE_PATH + "/#",
                LOGO_ID);
    }
}
