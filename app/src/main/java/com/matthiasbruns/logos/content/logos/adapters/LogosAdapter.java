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
import android.support.v7.util.SortedList;
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

    @NonNull
    private final SortedList<Logo> mLogos = new SortedList<>(Logo.class,
            new SortedList.Callback<Logo>() {
                @Override
                public boolean areContentsTheSame(final Logo oldItem, final Logo newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(final Logo item1, final Logo item2) {
                    return item1.getId() == item2.getId();
                }

                @Override
                public int compare(final Logo o1, final Logo o2) {
                    return Logo.ALPHABETICAL_COMPARATOR.compare(o1, o2);
                }

                @Override
                public void onChanged(final int position, final int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public void onInserted(final int position, final int count) {
                    notifyItemRangeInserted(position, count);
                }

                @Override
                public void onMoved(final int fromPosition, final int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }

                @Override
                public void onRemoved(final int position, final int count) {
                    notifyItemRangeRemoved(position, count);
                }
            });

    private WeakReference<Context> mContextWeakReference;

    @NonNull
    private List<ItemClickListener> mItemClickListeners;

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> mRequestBuilder;

    public LogosAdapter(final Context context) {
        mContextWeakReference = new WeakReference<>(context);
        mRequestBuilder = new GlideHelper().getSVGLoader(context);
        mItemClickListeners = new ArrayList<>();
    }

    public void add(Logo logo) {
        mLogos.add(logo);
    }

    public void add(List<Logo> logos) {
        mLogos.addAll(logos);
    }

    public void addClickListener(@NonNull final ItemClickListener itemClickListener) {
        mItemClickListeners.add(itemClickListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mLogos.size();
    }

    public Logo getLogo(final int id) {
        return mLogos.get(id);
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

    public void remove(Logo logo) {
        mLogos.remove(logo);
    }

    public void remove(List<Logo> logos) {
        mLogos.beginBatchedUpdates();
        for (Logo logo : logos) {
            mLogos.remove(logo);
        }
        mLogos.endBatchedUpdates();
    }

    public void removeClickListener(@NonNull final ItemClickListener itemClickListener) {
        mItemClickListeners.remove(itemClickListener);
    }

    public void replaceAll(final List<Logo> logos) {
        mLogos.beginBatchedUpdates();
        for (int i = mLogos.size() - 1; i >= 0; i--) {
            final Logo model = mLogos.get(i);
            if (!logos.contains(model)) {
                mLogos.remove(model);
            }
        }
        mLogos.addAll(logos);
        mLogos.endBatchedUpdates();
    }

    private void loadSVG(final Uri uri, final ImageView imageView) {
        mRequestBuilder
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                // SVG cannot be serialized so it's not worth to cache it
                .load(uri)
                .into(imageView);
    }

}