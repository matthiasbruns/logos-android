package com.matthiasbruns.logos.util;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mbruns on 06/12/2016.
 */

public class GlideHelper {

    /**
     * Decodes an SVG internal representation from an {@link InputStream}.
     */
    public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {

        public Resource<SVG> decode(InputStream source, int width, int height) throws IOException {
            try {
                SVG svg = SVG.getFromInputStream(source);
                return new SimpleResource<>(svg);
            } catch (SVGParseException ex) {
                throw new IOException("Cannot load SVG from stream", ex);
            }
        }

        @Override
        public String getId() {
            return "SvgDecoder.com.bumptech.svgsample.app";
        }
    }

    /**
     * Listener which updates the {@link ImageView} to be software rendered, because {@link
     * com.caverock.androidsvg.SVG SVG}/{@link android.graphics.Picture Picture} can't render on a
     * hardware backed {@link android.graphics.Canvas Canvas}.
     *
     * @param <T> not used, here to prevent unchecked warnings at usage
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class SvgSoftwareLayerSetter<T> implements RequestListener<T, PictureDrawable> {

        @Override
        public boolean onException(Exception e, T model, Target<PictureDrawable> target,
                boolean isFirstResource) {
            ImageView view = ((ImageViewTarget<?>) target).getView();
            if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
                view.setLayerType(ImageView.LAYER_TYPE_NONE, null);
            }
            return false;
        }

        @Override
        public boolean onResourceReady(PictureDrawable resource, T model,
                Target<PictureDrawable> target,
                boolean isFromMemoryCache, boolean isFirstResource) {
            if (target instanceof ImageViewTarget) {
                ImageView view = ((ImageViewTarget<?>) target).getView();
                if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
                    view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null);
                }
            }
            return false;
        }
    }

    /**
     * Convert the {@link SVG}'s internal representation to an Android-compatible one ({@link
     * Picture}).
     */
    public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {

        @Override
        public String getId() {
            return "";
        }

        @Override
        public Resource<PictureDrawable> transcode(Resource<SVG> toTranscode) {
            SVG svg = toTranscode.get();
            Picture picture = svg.renderToPicture();
            PictureDrawable drawable = new PictureDrawable(picture);
            return new SimpleResource<PictureDrawable>(drawable);
        }
    }

    public GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> getSVGLoader(
            final Context context) {
        return Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                //.placeholder(R.drawable.ic_facebook)
                //.error(R.drawable.ic_web)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }
}
