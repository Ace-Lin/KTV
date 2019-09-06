package com.newland.karaoke.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.newland.karaoke.R;

/**
 * 作为基类可以统一修改一些属性
 */
public abstract class BaseActivity extends AppCompatActivity {


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
     * 抽象方法，用于结束activity
     */
    public abstract void basefinish();


    @Override
    public void onBackPressed() {

        basefinish();
    }

}
