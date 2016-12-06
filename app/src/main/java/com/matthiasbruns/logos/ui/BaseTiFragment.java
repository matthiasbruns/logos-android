package com.matthiasbruns.logos.ui;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matthiasbruns.logos.R;

import net.grandcentrix.thirtyinch.TiFragment;
import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.TiView;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by mbruns on 06/12/2016.
 */

public abstract class BaseTiFragment<P extends TiPresenter<V>, V extends TiView>
        extends TiFragment<P, V> {

    protected Dialog showLoadingDialog(@Nullable final String title,
            @Nullable final String message,
            final boolean cancelable, final boolean show) {

        final MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.content(message);
        }

        builder.cancelable(cancelable);
        builder.progress(true, 0);

        final MaterialDialog dialog = builder.build();
        if (show) {
            dialog.show();
        }
        return dialog;
    }

    protected Dialog showLoadingDialog() {
        return showLoadingDialog(false, true);
    }

    protected Dialog showLoadingDialog(final boolean cancelable, final boolean show) {
        return showLoadingDialog(R.string.progress_dialog_loading_title,
                R.string.progress_dialog_loading_message, cancelable, show);
    }

    protected Dialog showLoadingDialog(final int title, final int message,
            final boolean cancelable, final boolean show) {
        final String titleString = getString(title);
        final String messageString = getString(message);

        return showLoadingDialog(titleString, messageString, cancelable, show);
    }
}
