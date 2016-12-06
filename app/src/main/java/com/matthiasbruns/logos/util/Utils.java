package com.matthiasbruns.logos.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

/**
 * Created by traxdata on 12/08/15.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static boolean equals(@Nullable Object o1, @Nullable Object o2) {
        return !((o1 != null && o2 != null && !o1.equals(o2))
                || (o1 == null && o2 != null)
                || (o1 != null && o2 == null));
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static int getIdFromUrl(final Uri uri) {
        return Integer.valueOf(uri.getLastPathSegment());
    }

    public static int getIdFromUrl(final String url) {
        final Uri uri = Uri.parse(url);
        return getIdFromUrl(uri);
    }

    public static void hideKeyboard(final Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isLandscape(final Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPotrait(final Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK)
                >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String readRawTextFile(final Context context, final int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public static void showKeyboard(final Context context, final EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        if (!TextUtils.isEmpty(editText.getText())) {
            editText.setSelection(editText.getText().length());
        }
    }
}