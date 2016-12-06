/*
Copyright 2015 Michal Pawlowski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.matthiasbruns.logos.util;

import com.matthiasbruns.logos.R;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Helper class that iterates through Toolbar views, and sets dynamically icons and texts color
 * Created by chomi3 on 2015-01-19.
 */
public class ToolbarColorizeHelper {

    private static final String TAG = ToolbarColorizeHelper.class.getSimpleName();

    /**
     * Use this method to colorize toolbar icons to the desired target color
     *
     * @param toolbarView       toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     * @param activity          reference to activity needed to register observers
     */
    public static void colorizeToolbar(final Toolbar toolbarView, int toolbarIconsColor,
            Activity activity) {
        Logger.d(TAG, "Applying color: " + toolbarIconsColor);

        final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(toolbarIconsColor,
                PorterDuff.Mode.MULTIPLY);

        for (int i = 0; i < toolbarView.getChildCount(); i++) {
            final View v = toolbarView.getChildAt(i);

            //Step 1 : Changing the color of back button (or open drawer button).
            if (v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton) v).getDrawable().setColorFilter(colorFilter);
            }

            if (v instanceof ActionMenuView) {
                for (int j = 0; j < ((ActionMenuView) v).getChildCount(); j++) {

                    //Step 2: Changing the color of any ActionMenuViews - icons that are not back button, nor text, nor overflow menu icon.
                    //Colorize the ActionViews -> all icons that are NOT: back button | overflow menu
                    final View innerView = ((ActionMenuView) v).getChildAt(j);
                    if (innerView instanceof ActionMenuItemView) {
                        for (int k = 0;
                                k < ((ActionMenuItemView) innerView).getCompoundDrawables().length;
                                k++) {
                            if (((ActionMenuItemView) innerView).getCompoundDrawables()[k]
                                    != null) {
                                final int finalK = k;

                                //Important to set the color filter in seperate thread, by adding it to the message queue
                                //Won't work otherwise.
                                innerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ActionMenuItemView) innerView)
                                                .getCompoundDrawables()[finalK]
                                                .setColorFilter(colorFilter);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            //Step 3: Changing the color of title and subtitle.
            toolbarView.setTitleTextColor(toolbarIconsColor);
            toolbarView.setSubtitleTextColor(toolbarIconsColor);

            //Step 4: Changing the color of the Overflow Menu icon.
            setOverflowButtonColor(toolbarView, activity, colorFilter);
        }
    }

    private static void removeOnGlobalLayoutListener(View v,
            ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * It's important to set overflowDescription atribute in styles, so we can grab the reference to
     * the overflow icon. Check: res/values/styles.xml
     */
    private static void setOverflowButtonColor(final Toolbar toolbarView, final Activity activity,
            final PorterDuffColorFilter colorFilter) {
        final String overflowDescription = activity
                .getString(R.string.accessibility_overflow);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

        decorView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // The List that contains the matching views
                final ArrayList<View> outViews = new ArrayList<>();
                // Traverse the view-hierarchy and locate the overflow button
                toolbarView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                // Guard against any errors
                if (outViews.isEmpty()) {
                    return;
                }
                for (View outView : outViews) {
                    if (outView instanceof ImageView) {
                        ImageView overflow = (ImageView) outView;
                        overflow.setColorFilter(colorFilter);
                    }
                }
            }

        }, 250);
    }
}