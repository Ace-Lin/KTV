package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVOrderInfo;

import org.litepal.LitePal;

public class OrderDetailActivity extends BaseActivity {
    private int id=-1;
    private TextView txt_title;
    private Toolbar commonToolBar;
    private KTVOrderInfo currentOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initData();
        initUI();
        initEvent();

    }

    private  void initUI(){
        hideStatusBar();
        commonToolBar = (Toolbar)findViewById(R.id.setting_toolbar);
        commonToolBar.setNavigationIcon(R.drawable.icon_back_left);
        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOrder=new Intent(OrderDetailActivity.this,OrderActivity.class);
                startActivity(intentOrder);
                finish();
            }
        });
        txt_title = findViewById(R.id.setting_title);

    }

    private void initData(){
        Bundle bundle=getIntent().getExtras();
        id=bundle.getInt("id");
        currentOrder= LitePal.where("id=?",String.valueOf(id)).findFirst(KTVOrderInfo.class,true);

    }

    private void initEvent(){

    }

    @Override
    public void basefinish() {
        finish();
    }
}
