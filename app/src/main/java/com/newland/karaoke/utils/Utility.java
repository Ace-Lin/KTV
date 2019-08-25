package com.newland.karaoke.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;

public class Utility {

    /**
     *   照片路径转化为图片返回
     */

    public static   Bitmap  getPirBitMap(String path)
    {
        File file = new File(path);
        if(file.exists())
            return BitmapFactory.decodeFile(path);

        return null;
    }

}
