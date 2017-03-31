package io.intrepid.contest.utils;


import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

public class ScreenshotHelperUtil {

    public static Bitmap getScreenshotFromRV(RecyclerView rv) {
        rv.setDrawingCacheEnabled(true);
        rv.buildDrawingCache(true);
        Bitmap bitmap  = Bitmap.createBitmap(rv.getDrawingCache());
        rv.setDrawingCacheEnabled(false); // clear drawing cache
        return bitmap;
    }
}
