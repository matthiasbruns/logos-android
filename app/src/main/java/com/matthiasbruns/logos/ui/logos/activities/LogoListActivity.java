package com.matthiasbruns.logos.ui.logos.activities;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.ui.BaseActivity;
import com.matthiasbruns.logos.ui.logos.fragments.LogoListFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by mbruns on 30/11/2016.
 */

public class LogoListActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LogoListFragment()).commit();
        }
    }
}
