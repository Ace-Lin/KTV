package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.newland.karaoke.R;

/**
 * 作为显示所有商品和房间信息的fragmen的载体
 */
public class DetailsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
    }
}
