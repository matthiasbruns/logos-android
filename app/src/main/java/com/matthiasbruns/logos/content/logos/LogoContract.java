package com.matthiasbruns.logos.content.logos;

import com.matthiasbruns.logos.BuildConfig;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoContract {

    /**
     * Logo model contact
     */
    public interface Logo {

        /**
         * All database columns
         */
        class Columns implements BaseColumns {

            public static final String EXTERNAL_ID = "external_id";

            public static final String LOGO_URL = "logo_url";

            public static final String NAME = "name";

            public static final String SHORT_NAME = "short_name";

            public static final String SOURCE = "source";

            public static final String URL = "url";
        }

        /**
         * Custom device query builder
         */
        class QueryBuilder implements com.matthiasbruns.logos.content.QueryBuilder {

            private static QueryBuilder sInstance;

            @Override
            public String findById(final long id) {
                return Columns._ID + " = " + id;
            }

            public static QueryBuilder getInstance() {
                if (sInstance == null) {
                    sInstance = new QueryBuilder();
                }
                return sInstance;
            }
        }

        /**
         * Custom UriBuilder which generated Content Provider URIs
         */
        class UriBuilder
                implements com.matthiasbruns.logos.content.UriBuilder<Logo> {

            private static UriBuilder sInstance;

            @Override
            public Uri buildDeleteUri() {
                return CONTENT_URI;
            }

            @Override
            public Uri buildDeleteUri(long id) {
                return buildQueryUri(id);
            }

            @Override
            public Uri buildDeleteUri(final Logo logo) {
                return buildUpdateUri(logo);
            }

            @Override
            public Uri buildInsertUri() {
                return CONTENT_URI;
            }

            @Override
            public Uri buildQueryUri(final Logo logo) {
                return buildQueryUri(logo.getId());
            }

            @Override
            public Uri buildQueryUri(long id) {
                return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
            }

            @Override
            public Uri buildUpdateUri(final Logo logo) {
                return buildQueryUri(logo.getId());
            }

            @Override
            public Uri buildUpdateUri(long id) {
                return buildQueryUri(id);
            }

            public static UriBuilder getInstance() {
                if (sInstance == null) {
                    sInstance = new UriBuilder();
                }
                return sInstance;
            }
        }

        /**
         * Helpder interface which stored pre defined projections
         */
        interface Projection {

            String[] SELECT_ALL = new String[]{
                    Columns._ID,
                    Columns.EXTERNAL_ID,
                    Columns.LOGO_URL,
                    Columns.NAME,
                    Columns.SHORT_NAME,
                    Columns.SOURCE,
                    Columns.URL
            };
        }

        String BASE_PATH = "logo";
        Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, BASE_PATH);
        String TABLE_NAME = "LOGOS";
        String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                + AUTHORITY + ".logo";
        String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY
                + ".logos";

        /**
         * @return a ready to store content values object with all model data in it
         */
        ContentValues getContentValues();

        long getId();
    }

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".logo";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
}
