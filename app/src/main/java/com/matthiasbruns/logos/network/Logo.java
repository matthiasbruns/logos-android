package com.matthiasbruns.logos.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mbruns on 30/11/2016.
 */
public class Logo {

    @SerializedName("id")
    public String mId;

    @SerializedName("logoURL")
    public String mLogoUrl;

    @SerializedName("name")
    public String mName;

    @SerializedName("path")
    public String mPath;

    @SerializedName("shortname")
    public String mShortName;

    @SerializedName("source")
    public String mSource;

    @SerializedName("url")
    public String mUrl;
}
