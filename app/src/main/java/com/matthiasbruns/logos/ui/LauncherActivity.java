package com.matthiasbruns.logos.ui;

import com.matthiasbruns.logos.R;
import com.matthiasbruns.logos.ui.logos.activities.LogoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class LauncherActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(getApplicationContext(), LogoActivity.class));
    }
}
