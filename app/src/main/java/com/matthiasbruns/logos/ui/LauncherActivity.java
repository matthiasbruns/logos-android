package com.matthiasbruns.logos.ui;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.ui.logos.activities.LogoListActivity;

import android.content.Intent;
import android.os.Bundle;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(getApplicationContext(), LogoListActivity.class));
    }
}
