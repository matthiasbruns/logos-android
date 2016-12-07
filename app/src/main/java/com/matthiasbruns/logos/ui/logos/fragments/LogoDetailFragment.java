package com.matthiasbruns.logos.ui.logos.fragments;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.content.logos.Logo;
import com.matthiasbruns.logos.ui.BaseTiFragment;
import com.matthiasbruns.logos.ui.logos.presenters.LogoDetailPresenter;
import com.matthiasbruns.logos.ui.logos.views.LogoDetailView;
import com.matthiasbruns.logos.util.Coloring;
import com.matthiasbruns.logos.util.ToolbarColorizeHelper;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoDetailFragment extends BaseTiFragment<LogoDetailPresenter, LogoDetailView>
        implements LogoDetailView {

    @BindView(R.id.collapsing_toolbar)
    protected CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.coordinator_layout)
    protected CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.btn_logo_detail_company)
    protected Button mLogoCompanyButton;

    @BindView(R.id.btn_logo_detail_download)
    protected Button mLogoDownloadButton;

    @BindView(R.id.img_logo_image)
    protected ImageView mLogoImageView;

    @BindView(R.id.txt_logo_source)
    protected TextView mLogoSourceTextView;

    @BindView(R.id.txt_logo_url)
    protected TextView mLogoUrlTextView;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private Dialog mLoadingDialog;

    private Long mLogoId;

    private Observable<Void> mOnLogoClicked;

    private ShareActionProvider mShareActionProvider;

    @Override
    public void applyLogoColor(final int rgb) {
        // Toolbar colors
        mCollapsingToolbar.setBackgroundColor(rgb);
        mCollapsingToolbar.setContentScrimColor(rgb);
        mCollapsingToolbar.setStatusBarScrimColor(rgb);
        ToolbarColorizeHelper.colorizeToolbar(mToolbar, rgb, getActivity());
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(rgb);
        }

        // Link colors
        mLogoUrlTextView.setLinkTextColor(rgb);

        // Button colors
        final Coloring coloring = Coloring.get();
        final Drawable buttonDrawable1 = coloring
                .createBackgroundDrawable(rgb, coloring.lightenColor(rgb),
                        coloring.darkenColor(rgb), true);
        final Drawable buttonDrawable2 = coloring
                .createBackgroundDrawable(rgb, coloring.lightenColor(rgb),
                        coloring.darkenColor(rgb), true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mLogoCompanyButton.setBackground(buttonDrawable1);
            mLogoDownloadButton.setBackground(buttonDrawable2);
        } else {
            mLogoCompanyButton.setBackgroundDrawable(buttonDrawable1);
            mLogoDownloadButton.setBackgroundDrawable(buttonDrawable2);
        }
    }

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
        setHasOptionsMenu(true);
        Bundle bundle = savedInstanceState;
        if (bundle == null) {
            bundle = getArguments();
        }
        mLogoId = bundle.getLong(EXTRA_LOGO_ID);
    }


    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_logo_detail, container, false);
        ButterKnife.bind(this, view);
        mOnLogoClicked = RxView.clicks(mLogoImageView);
        getBaseActivity().setSupportActionBar(mToolbar);
        return view;
    }

    @Override
    public void onData(@Nullable final Logo logo) {
        mCollapsingToolbar.setTitle(logo.getName());
        mLogoSourceTextView.setText(logo.getSource());
        mLogoUrlTextView.setText(logo.getUrl());
    }

    @NonNull
    @Override
    public Observable<Void> onLogoClicked() {
        return mOnLogoClicked;
    }

    @NonNull
    @Override
    public LogoDetailPresenter providePresenter() {
        return new LogoDetailPresenter();
    }

    @Override
    public void rotateLogo() {
        mLogoImageView.animate().rotation(360).start();
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
    public void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void showError(final String error) {
        showErrorRx(error).subscribe();
    }

    @NonNull
    @Override
    public Observable<Integer> showErrorRx(final String error) {
        return Observable.create(subscriber -> {
            final MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
            builder.title(R.string.dialog_error_title)
                    .content(error)
                    .cancelable(false)
                    .negativeText(R.string.dialog_action_ok)
                    .onNegative(
                            (materialDialog, dialogAction) -> subscriber
                                    .onNext(DIALOG_BUTTON_NEGATIVE))
                    .show();
        });
    }
}
