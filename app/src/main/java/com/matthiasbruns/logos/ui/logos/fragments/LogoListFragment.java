package com.matthiasbruns.logos.ui.logos.fragments;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.ui.BaseTiFragment;
import com.matthiasbruns.logos.ui.logos.presenters.LogoListPresenter;
import com.matthiasbruns.logos.ui.logos.views.LogoListView;
import com.matthiasbruns.logos.util.Utils;
import com.matthiasbruns.logos.views.VerticalSpaceItemDecoration;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.subjects.PublishSubject;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogoListFragment extends BaseTiFragment<LogoListPresenter, LogoListView>
        implements LogoListView {

    private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextChange(final String query) {
            mOnSearchChanged.onNext(query);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(final String query) {
            Utils.hideKeyboard(getActivity());
            return true;
        }
    }

    @NonNull
    private final PublishSubject<String> mOnSearchChanged = PublishSubject.create();

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private LinearLayoutManager mLayoutManager;

    private Parcelable mLayoutManagerSavedState;

    @Nullable
    private Dialog mLoadingDialog;

    private int mRetainedScrollPosition = 0;

    @Override
    public int getRetainedScrollPosition() {
        return mRetainedScrollPosition;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchListener());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_logo_list, container, false);

        if (savedInstanceState != null) {
            mRetainedScrollPosition = savedInstanceState.getInt(EXTRA_SCROLL_POSITION);
            mLayoutManagerSavedState = savedInstanceState.getParcelable(EXTRA_SAVED_LAYOUT_MANAGER);
        }

        ButterKnife.bind(this, view);
        getBaseActivity().setSupportActionBar(mToolbar);
        return view;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLayoutManager != null) {
            mRetainedScrollPosition = mLayoutManager
                    .findFirstVisibleItemPosition();
            outState.putInt(EXTRA_SCROLL_POSITION, mRetainedScrollPosition);
        }

        if (mRecyclerView != null) {
            mLayoutManagerSavedState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(EXTRA_SAVED_LAYOUT_MANAGER, mLayoutManagerSavedState);
        }
    }

    @NonNull
    @Override
    public PublishSubject<String> onSearchChanged() {
        return mOnSearchChanged;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());
    }

    @NonNull
    @Override
    public LogoListPresenter providePresenter() {
        return new LogoListPresenter();
    }

    @Override
    public void restoreLayoutManagerState() {
        if (mLayoutManagerSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        }
    }

    @Override
    public void scrollToPosition(final int retainedScrollPosition) {
       /* if (mLayoutManager != null) {
            mLayoutManager.scrollToPosition(retainedScrollPosition);
        }*/
    }

    @Override
    public void setAdapter(@NonNull final RecyclerView.Adapter<?> adapter) {
        mRecyclerView.setAdapter(adapter);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void startLoading() {
        mLoadingDialog = showLoadingDialog();
    }

    @Override
    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}