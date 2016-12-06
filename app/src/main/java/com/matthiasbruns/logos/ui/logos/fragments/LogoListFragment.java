package com.matthiasbruns.logos.ui.logos.fragments;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.ui.BaseTiFragment;
import com.matthiasbruns.logos.ui.logos.presenters.LogoListPresenter;
import com.matthiasbruns.logos.ui.logos.views.LogoListView;
import com.matthiasbruns.logos.views.VerticalSpaceItemDecoration;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogoListFragment extends BaseTiFragment<LogoListPresenter, LogoListView>
        implements LogoListView {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logo_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
    }

    @NonNull
    @Override
    public LogoListPresenter providePresenter() {
        return new LogoListPresenter();
    }

    @Override
    public void setAdapter(@NonNull final RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void stopLoading() {

    }
}