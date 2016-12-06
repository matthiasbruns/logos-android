package com.matthiasbruns.logos.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mbruns on 06/12/2016.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration() {
        this.verticalSpaceHeight = 48;
    }

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {
        outRect.top = verticalSpaceHeight / 2;
        outRect.bottom = verticalSpaceHeight / 2;
    }
}