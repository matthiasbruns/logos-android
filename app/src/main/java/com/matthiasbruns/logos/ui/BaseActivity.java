package com.matthiasbruns.logos.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mbruns on 06/12/2016.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
