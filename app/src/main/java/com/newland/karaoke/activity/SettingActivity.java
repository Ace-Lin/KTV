package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;


import com.newland.karaoke.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar mToolbar =  (Toolbar)findViewById(R.id.setting_toolbar);
        setSupportActionBar(mToolbar);
        setBackArrow();
    }



    @Override
    public void basefinish() {
        finish();
    }
}
