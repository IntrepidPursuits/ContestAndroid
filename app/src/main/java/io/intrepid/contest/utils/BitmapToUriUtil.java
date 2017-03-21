package io.intrepid.contest.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class BitmapToUriUtil {

    public static Uri convert(Context context, Bitmap bitmap) {
        Uri uri = null;
        try {
            File file = new File(context.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
           e.printStackTrace();
        }
        return uri;
    }
}
