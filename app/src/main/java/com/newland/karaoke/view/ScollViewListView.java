package com.newland.karaoke.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * 解决ScollView嵌套ListView 内容显示不全
 */
public class ScollViewListView extends ListView {

    public ScollViewListView(Context context) {
        super(context);
    }

    public ScollViewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScollViewListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
