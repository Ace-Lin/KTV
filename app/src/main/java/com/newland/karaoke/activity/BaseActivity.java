package com.newland.karaoke.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.newland.karaoke.R;
import com.newland.karaoke.utils.LogUtil;

/**
 * 作为基类可以统一修改一些属性
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar commonToolBar;

    /**
     * 隐藏状态栏
     */
  public void hideStatusBar(){

      if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
          getWindow().setStatusBarColor(Color.TRANSPARENT);//防止5.x以后半透明影响效果，使用这种透明方式
      }
  }

    /**
     * 显示toolbar
     */
    protected void showToolBar(int titleID) {

        commonToolBar = findViewById(R.id.setting_toolbar);
        commonToolBar.setNavigationIcon(R.drawable.icon_back_left);
        commonToolBar.setNavigationOnClickListener(v -> basefinish());
      //设置title
      TextView txt_title = findViewById(R.id.setting_title);
      txt_title.setText(titleID);
    }

    /**
     * 抽象方法，用于结束activity
     */
    public abstract void basefinish();


    @Override
    public void onBackPressed() {

        basefinish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.error("onStart",getClass());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.error("onResume",getClass());
    }


    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.error("onPause",getClass());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.error("onStop",getClass());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.error("onRestart",getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.error("onDestroy",getClass());
    }

}
