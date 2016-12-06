package com.matthiasbruns.logos.ui.logos.activities;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.ui.BaseActivity;
import com.matthiasbruns.logos.ui.logos.fragments.LogoDetailFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by mbruns on 06/12/2016.
 */

public class LogoDetailActivity extends BaseActivity {

    private static final String TAG = LogoDetailActivity.class.getName();

    public static final String EXTRA_LOGO_ID = TAG + "_logo_id";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        if (savedInstanceState == null) {
            LogoDetailFragment fragment = LogoDetailFragment
                    .create(getIntent().getLongExtra(EXTRA_LOGO_ID, 0L));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    public static Intent create(final Context context, final long logoId) {
        final Intent intent = new Intent(context, LogoDetailActivity.class);
        final Bundle bundle = new Bundle();
        intent.putExtra(EXTRA_LOGO_ID, logoId);
        return intent;
    }
}
