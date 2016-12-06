package com.matthiasbruns.logos.content.logos.adapters;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caverock.androidsvg.SVG;
import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.content.logos.Logo;
import com.matthiasbruns.logos.util.GlideHelper;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mbruns on 01/12/2016.
 */

public class LogosAdapter extends RecyclerView.Adapter<LogosAdapter.ViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.logo_image)
        ImageView mImageView;

        @BindView(R.id.logo_title)
        TextView mTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            Log.d(TAG, "onClick " + getAdapterPosition());
            for (final ItemClickListener listener : mItemClickListeners) {
                listener.onClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {

        void onClick(final View view, final int position);
    }

    private static final String TAG = LogosAdapter.class.getName();

    private WeakReference<Context> mContextWeakReference;

    @NonNull
    private List<ItemClickListener> mItemClickListeners;

    private List<Logo> mLogos;

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public LogosAdapter(final Context context) {
        mContextWeakReference = new WeakReference<>(context);
        mLogos = new ArrayList<>();
        mRequestBuilder = new GlideHelper().getSVGLoader(context);
        mItemClickListeners = new ArrayList<>();
    }

    public void addClickListener(@NonNull final ItemClickListener itemClickListener) {
        mItemClickListeners.add(itemClickListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mLogos.size();
    }

    public List<Logo> getLogos() {
        return mLogos;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Logo logo = mLogos.get(position);
        holder.mTextView.setText(logo.mName);
        loadSVG(Uri.parse(logo.mLogoUrl), holder.mImageView);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LogosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        // create a new view
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_logo, parent, false);

        return new ViewHolder(view);
    }

    public void removeClickListener(@NonNull final ItemClickListener itemClickListener) {
        mItemClickListeners.remove(itemClickListener);
    }

    public void setLogos(final List<Logo> logos) {
        mLogos.clear();
        mLogos.addAll(logos);
        notifyDataSetChanged();
    }

    private void loadSVG(final Uri uri, final ImageView imageView) {
        mRequestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                // SVG cannot be serialized so it's not worth to cache it
                .load(uri)
                .into(imageView);
    }

}