package com.newland.karaoke.activity;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.newland.karaoke.R;

/**
 * 作为基类可以统一修改一些属性
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 设置左上角back按钮
     */
    public void setBackArrow() {

        final Drawable upArrow = getResources().getDrawable(R.drawable.icon_back_left);
        //给ToolBar设置左侧的图标
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置actionBar的标题是否显示，对应ActionBar.DISPLAY_SHOW_TITLE。
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    /**
     * 点击左上角的返回按钮，结束本Activity
     * home就是左上角的小箭头，在toolbar上
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            basefinish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
