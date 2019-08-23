package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;

import org.litepal.LitePal;

public class OrderActivity extends AppCompatActivity {

    private TextView tv_order_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initUI();
    }

    private void initUI(){
        tv_order_info=findViewById(R.id.tv_order_info);

    }
}
