package com.matthiasbruns.logos.ui.logos.fragments;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.content.logos.Logo;
import com.matthiasbruns.logos.ui.BaseTiFragment;
import com.matthiasbruns.logos.ui.logos.presenters.LogoDetailPresenter;
import com.matthiasbruns.logos.ui.logos.views.LogoDetailView;
import com.matthiasbruns.logos.util.ToolbarColorizeHelper;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoDetailFragment extends BaseTiFragment<LogoDetailPresenter, LogoDetailView>
        implements LogoDetailView {

    @BindView(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.logo_image)
    protected ImageView mLogoImageView;

    @BindView(R.id.logo_title)
    protected TextView mLogoTitleTextView;

    private Dialog mLoadingDialog;

    private Long mLogoId;

    public static LogoDetailFragment create(final long logoId) {
        final LogoDetailFragment fragment = new LogoDetailFragment();
        final Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_LOGO_ID, logoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public long getLogoId() {
        return mLogoId;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = savedInstanceState;
        if (bundle == null) {
            bundle = getArguments();
        }
        mLogoId = bundle.getLong(EXTRA_LOGO_ID);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_logo_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onData(@Nullable final Logo logo) {
        mLogoTitleTextView.setText(logo.getName());
    }

    @NonNull
    @Override
    public LogoDetailPresenter providePresenter() {
        return new LogoDetailPresenter();
    }

    @Override
    public void setLoading(final boolean loading) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            // Only dismiss if we have a dialog
            if (!loading) {
                mLoadingDialog.dismiss();
            }
        } else if (loading) {
            // only create a new one, if there is no dialog
            mLoadingDialog = showLoadingDialog();
        }
    }

    @Override
    public void setLogoImage(@NonNull final Drawable drawable) {
        mLogoImageView.setImageDrawable(drawable);
    }

    @Override
    public void setToolbarColor(final int rgb) {
        mCollapsingToolbar.setBackgroundColor(rgb);
        mCollapsingToolbar.setContentScrimColor(rgb);
        mCollapsingToolbar.setStatusBarScrimColor(rgb);
    }

    @Override
    public void showError(final String error) {
        showErrorRx(error).subscribe();
    }

    @NonNull
    @Override
    public Observable<Integer> showErrorRx(final String error) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                final MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
                builder.title(R.string.dialog_error_title)
                        .content(error)
                        .cancelable(false)
                        .negativeText(R.string.dialog_action_ok)
                        .onNegative(
                                (materialDialog, dialogAction) -> subscriber
                                        .onNext(DIALOG_BUTTON_NEGATIVE))
                        .show();
            }
        });
    }
}
