package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.newland.karaoke.R;

/**
 * 作为添加商品和房间信息的fragmen的载体
 */
public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }
}
