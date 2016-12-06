package com.matthiasbruns.logos.content.logos;

import com.google.gson.annotations.SerializedName;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by mbruns on 30/11/2016.
 */
public class Logo implements LogoContract.Logo {

    @SerializedName("id")
    public String mExternalId;

    public long mId;

    @SerializedName("logoURL")
    public String mLogoUrl;

    @SerializedName("name")
    public String mName;

    @SerializedName("shortname")
    public String mShortName;

    @SerializedName("source")
    public String mSource;

    @SerializedName("url")
    public String mUrl;

    public Logo() {
    }

    public Logo(final Cursor cursor) {
        mId = cursor.getLong(cursor.getColumnIndex(Columns._ID));
        mExternalId = cursor.getString(cursor.getColumnIndex(Columns.EXTERNAL_ID));
        mLogoUrl = cursor.getString(cursor.getColumnIndex(Columns.LOGO_URL));
        mName = cursor.getString(cursor.getColumnIndex(Columns.NAME));
        mShortName = cursor.getString(cursor.getColumnIndex(Columns.SHORT_NAME));
        mSource = cursor.getString(cursor.getColumnIndex(Columns.SOURCE));
        mUrl = cursor.getString(cursor.getColumnIndex(Columns.URL));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Logo logo = (Logo) o;

        if (mId != logo.mId) {
            return false;
        }
        if (mExternalId != null ? !mExternalId.equals(logo.mExternalId)
                : logo.mExternalId != null) {
            return false;
        }
        return mSource != null ? mSource.equals(logo.mSource) : logo.mSource == null;

    }

    /**
     * @return a ready to store content values object with all model data in it
     */
    @Override
    public ContentValues getContentValues() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(Columns._ID, mId);
        contentValues.put(Columns.EXTERNAL_ID, mExternalId);
        contentValues.put(Columns.LOGO_URL, mLogoUrl);
        contentValues.put(Columns.NAME, mName);
        contentValues.put(Columns.SHORT_NAME, mShortName);
        contentValues.put(Columns.SOURCE, mSource);
        contentValues.put(Columns.URL, mUrl);
        return contentValues;
    }

    public String getExternalId() {
        return mExternalId;
    }

    @Override
    public long getId() {
        return mId;
    }

    public String getLogoUrl() {
        return mLogoUrl;
    }

    public String getName() {
        return mName;
    }

    public String getShortName() {
        return mShortName;
    }

    public String getSource() {
        return mSource;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public int hashCode() {
        int result = mExternalId != null ? mExternalId.hashCode() : 0;
        result = 31 * result + (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mSource != null ? mSource.hashCode() : 0);
        return result;
    }

    public void setExternalId(final String externalId) {
        mExternalId = externalId;
    }

    public void setId(final long id) {
        mId = id;
    }

    public void setLogoUrl(final String logoUrl) {
        mLogoUrl = logoUrl;
    }

    public void setName(final String name) {
        mName = name;
    }

    public void setShortName(final String shortName) {
        mShortName = shortName;
    }

    public void setSource(final String source) {
        mSource = source;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }
}
