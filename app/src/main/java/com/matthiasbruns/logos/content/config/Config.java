package com.matthiasbruns.logos.content.config;

import net.grandcentrix.tray.TrayPreferences;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by mbruns on 06/12/2016.
 */

public class Config extends TrayPreferences {

    private static final String MODULE = "config";

    private static final int VERSION = 1;

    private static final String KEY_FIRST_LAUNCH = "KEY_FIRST_LAUNCH";

    public Config(@NonNull final Context context) {
        super(context, MODULE, VERSION);
    }

    public boolean isFirstLaunch() {
        return getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(final boolean firstLaunch) {
        put(KEY_FIRST_LAUNCH, firstLaunch);
    }
}
