package com.newland.karaoke.activity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.newland.karaoke.R;

/**
 * 作为基类可以统一修改一些属性
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 通用的ToolBar标题
     */
    private TextView commonTitle;
    /**
     * 通用的ToolBar
     */
    private Toolbar commonToolBar;


   public void initBaseView(int id)
   {
       commonToolBar =  (Toolbar)findViewById(id);
       commonTitle=(TextView)findViewById(R.id.setting_title);
       setSupportActionBar(commonToolBar);
       setBackArrow();
   }

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

    /**
     * 设置标题
     *
     * @param title
     */
    public void setToolBarTitle(String title) {
        commonTitle.setText(title);
    }

    /**
     * 子类调用，重新设置Toolbar
     *
     * @param layout
     */
    public void setToolBar(int layout) {
        hidetoolBar();
        commonToolBar = (Toolbar)findViewById(layout);
        setSupportActionBar(commonToolBar);
        //设置actionBar的标题是否显示，对应ActionBar.DISPLAY_SHOW_TITLE。
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 隐藏ToolBar，通过setToolBar重新定制ToolBar
     */
    public void hidetoolBar() {
        commonToolBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {

        basefinish();
    }

}
